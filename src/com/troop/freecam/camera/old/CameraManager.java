package com.troop.freecam.camera.old;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.SurfaceHolder;

import com.troop.freecam.MainActivity;
import com.troop.freecam.manager.AppSettingsManager;
import com.troop.freecam.manager.AutoFocusManager;
import com.troop.freecam.manager.ExifManager;
import com.troop.freecam.manager.HdrManager;
import com.troop.freecam.manager.ManualFocus;
import com.troop.freecam.manager.MediaScannerManager;
import com.troop.freecam.manager.camera_parameters.ParametersManager;
import com.troop.freecam.surfaces.CamPreview;
import com.troop.freecam.utils.DeviceUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by troop on 25.08.13.
 */
public class CameraManager extends VideoCam implements SurfaceHolder.Callback , SensorEventListener
{
    final String TAG = "freecam.CameraManager";
    CameraManager cameraManager;

    public boolean Running = false;
    public AutoFocusManager autoFocusManager;
    public static final String KEY_CAMERA_INDEX = "camera-index";
    public static final String KEY_S3D_SUPPORTED_STR = "s3d-supported";
    public boolean touchtofocus = false;
    public MainActivity activity;
    public ManualFocus manualFocus;
    public HdrManager HdrRender;
    public boolean takePicture = false;
    public boolean isRdy = false;
    private Camera.OnZoomChangeListener onZoomChangeListener;



    public CameraManager(CamPreview context, MainActivity activity, AppSettingsManager appSettingsManager)
    {
        super(context, appSettingsManager);
        this.activity = activity;
        Log.d(TAG, "Loading CameraManager");
        context.getHolder().addCallback(this);

        autoFocusManager = new AutoFocusManager(this, activity);
        cameraManager = this;
        manualFocus = new ManualFocus(this);
        HdrRender = new HdrManager(this);
        parametersManager = new ParametersManager(this, appSettingsManager);

        Log.d(TAG, "Loading CameraManager done");
    }

    //Remaining Pictures Ca;cu;ation
    public int RemainingPics ()
    {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;

        int mb = (int) megAvailable / 10;

        return mb;
    }
//WIP
    public int VideoLength ()
    {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;

        int mb = (int) megAvailable / 10;

        return mb;
    }
    //Aspect ratio Calc
    public String Aspect()
    {
        int w = parametersManager.getParameters().getPictureSize().width;
        int h = parametersManager.getParameters().getPictureSize().height;

        String box = "4:3";
        String wide = "16:9";

        double ar = w / h;


        if (ar == 1.777777777777778)
            return wide;

        if(ar == 1.333333333333333)
            return box;

        return box;
    }
    
    //On Picture Size Detect Do Action
    public void DoAr()
    {
        if (Double.parseDouble(Aspect()) == 1.777777777777778)
            parametersManager.getParameters().setPreviewSize(1920,1080);

        if(DeviceUtils.isG2())
            if(Double.parseDouble(Aspect()) == 1.333333333333333)
                parametersManager.getParameters().setPreviewSize(1440,1080);

        if(Double.parseDouble(Aspect()) == 1.333333333333333)
            parametersManager.getParameters().setPreviewSize(1440,1080);
    }

    Bitmap bitmascale;
    @Override
    public void onPictureSaved(File file)
    {
        takePicture = false;
        if(bitmascale != null)
            bitmascale.recycle();
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 32;
            Bitmap bitmaporg = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            //mediaScannerManager.startScan(s);

            int w = cameraManager.activity.thumbButton.getWidth();
            int h = cameraManager.activity.thumbButton.getHeight();
            bitmascale = Bitmap.createScaledBitmap(bitmaporg,w,h,true);
            cameraManager.activity.thumbButton.setImageBitmap(bitmascale);
            cameraManager.lastPicturePath =file.getAbsolutePath();
            MediaScannerManager.ScanMedia(activity,file);
            bitmaporg.recycle();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        ExifManager m = new ExifManager();
        try {
            m.LoadExifFrom(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        onShutterSpeed(m.getExposureTime());

    }

    public  void Start()
    {
        OpenCamera();
    }

    public void SetOnZoomChangedListner(Camera.OnZoomChangeListener onZoomChangeListener)
    {
        this.onZoomChangeListener = onZoomChangeListener;
    }

    @Override
    protected void OpenCamera() {
        super.OpenCamera();
        isRdy = false;
        try {
            mCamera.setPreviewDisplay(activity.mPreview.mHolder);
            mCamera.setZoomChangeListener(onZoomChangeListener);
            if(Settings.OrientationFix.GET() == true)
                fixCameraDisplayOrientation();
        } catch (Exception exception) {
            CloseCamera();

            // TODO: add more exception handling logic here
        }
        isRdy = true;
    }

    private void fixCameraDisplayOrientation()
    {
        String tmp = Settings.Cameras.GetCamera();

        if(!tmp.equals(AppSettingsManager.Preferences.MODE_3D) && !tmp.equals(AppSettingsManager.Preferences.MODE_2D))
        {
            mCamera.setDisplayOrientation(0);
            //mParameters.setRotation(0);
        }
        else
        {
            mCamera.setDisplayOrientation(180);
            //mParameters.setRotation(180);
        }
    }


    /**
     * set the Camera.Parameters to the ParametersManager
     * @param restartPreview
     * if true preview is started
     */
    public  void ReloadCameraParameters(boolean restartPreview)
    {
        isRdy = false;
        if (restartPreview)
        {
            Log.d(TAG, "Camera is restarted");
            if (mCamera == null)
            {
                Log.e(TAG, "Camera was released");
                return;
            }
            parametersManager.SetCameraParameters(mCamera.getParameters());

            parametersManager.SetJpegQuality(100);
            mCamera.startPreview();
            //set camera_parameters again for the evo because the preview can be laggy if its not done...
            if (DeviceUtils.isEvo3d())
                mCamera.setParameters(parametersManager.getParameters());
        }
        else
        {
            mCamera.setParameters(parametersManager.getParameters());
            Log.d(TAG, "Params Updated");
        }
        isRdy = true;
    }



    public  void Stop()
    {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        isRdy = false;
        mCamera.stopPreview();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CloseCamera();
    }

    public void StartTakePicture()
    {

        //if cam is not working
        if(!IsWorking)
        {
            //if focus mode is touch to focus
            if(cameraManager.Settings.touchToFocusSetting.get())
            {
                //take pic directly
                TakePicture(crop);
            }
            else
            {
                if (!autoFocusManager.focusing && autoFocusManager.CanFocus() &&
                    parametersManager.getParameters().getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO) ||
                    parametersManager.getParameters().getFocusMode().equals(Camera.Parameters.FOCUS_MODE_MACRO))
                {
                    if (!autoFocusManager.hasFocus)
                    {
                        autoFocusManager.StartFocus();
                        autoFocusManager.takePicture = true;
                    }
                    else
                    {
                        TakePicture(crop);
                    }
                }

            }
        }
    }

    public void StartFocus()
    {
        if (true)
        {
            autoFocusManager.StartFocus();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
       ReloadCameraParameters(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        Start();
        Running = true;

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Stop();
        Running = false;

    }



    @Override
    public void onSensorChanged(SensorEvent event)
    {
        /*if (camera_parameters != null && camera_parameters.getFocusMode().equals("auto"))
        {
            if (takePicture == false)
            {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float deltaX  = Math.abs(mLastX - x);
                float deltaY = Math.abs(mLastY - y);
                float deltaZ = Math.abs(mLastZ - z);

                if ((deltaX > 15 || deltaY > 15 || deltaZ > 15) && autoFocusManager.focusing){ //AUTOFOCUS (while it is not autofocusing)
                    autoFocusManager.focusing = false;
                    mCamera.autoFocus(autoFocusManager);
                    mLastX = x;
                    mLastY = y;
                    mLastZ = z;
                }
            }
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}