package com.troop.freedcam.camera2;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.Log;
import android.view.TextureView;

import com.troop.freedcam.camera2.modules.ModuleHandlerApi2;
import com.troop.freedcam.camera2.parameters.ParameterHandlerApi2;
import com.troop.freedcam.i_camera.AbstractCameraUiWrapper;
import com.troop.freedcam.i_camera.parameters.I_ParametersLoaded;
import com.troop.freedcam.ui.AppSettingsManager;
import com.troop.freedcam.utils.StringUtils;

/**
 * Created by troop on 07.12.2014.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraUiWrapperApi2 extends AbstractCameraUiWrapper implements TextureView.SurfaceTextureListener, I_ParametersLoaded
{
    public BaseCameraHolderApi2 cameraHolder;
    Context context;
    AppSettingsManager appSettingsManager;
    TextureView preview;

    private static String TAG = StringUtils.TAG + CameraUiWrapperApi2.class.getSimpleName();

    public CameraUiWrapperApi2()
    {

    }

    public CameraUiWrapperApi2(Context context, TextureView preview, AppSettingsManager appSettingsManager)
    {
        super(null, appSettingsManager);
        this.preview = preview;
        preview.setSurfaceTextureListener(this);
        this.appSettingsManager = appSettingsManager;
        this.context = context;
        //attache the callback to the Campreview
        //preview.getHolder().addCallback(this);
        this.cameraHolder = new BaseCameraHolderApi2(context, this, uiHandler, appSettingsManager);
        super.cameraHolder = this.cameraHolder;
        camParametersHandler = new ParameterHandlerApi2(cameraHolder, appSettingsManager, uiHandler);
        cameraHolder.ParameterHandler = (ParameterHandlerApi2)camParametersHandler;
        moduleHandler = new ModuleHandlerApi2(cameraHolder, appSettingsManager);


        camParametersHandler.ParametersEventHandler.AddParametersLoadedListner(this);
        //preview.ParametersHandler = camParametersHandler;


        Focus = new FocusHandlerApi2(this);
        cameraHolder.Focus = Focus;
        Log.d(TAG, "Constructor done");

    }

    @Override
    protected void startCamera() {
        cameraHolder.OpenCamera(appSettingsManager.GetCurrentCamera());
        Log.d(TAG, "opencamera");
    }

    @Override
    protected void stopCamera()
    {
        Log.d(TAG, "Stop Camera");
        cameraHolder.CloseCamera();
    }

    @Override
    protected void startPreview()
    {
        Log.d(TAG, "Stop Preview");
        cameraHolder.StartPreview();
    }

    @Override
    protected void stopPreview()
    {
        Log.d(TAG, "Stop Preview");
        cameraHolder.StopPreview();
    }

    @Override
    public void ParametersLoaded() {
        //cameraHolder.StartPreview();
    }

    @Override
    public void onCameraOpen(String message)
    {
        cameraHolder.SetSurface(preview);
        cameraHolder.StartPreview();
        Log.d(TAG, "Camera Opened and Preview Started");
        super.onCameraOpen(message);
    }

    @Override
    public void onCameraClose(String message)
    {

    }

    @Override
    public void onPreviewOpen(String message) {

    }

    @Override
    public void onPreviewClose(String message) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        Log.d(TAG, "SurfaceTextureAvailable");
        PreviewSurfaceRdy = true;
        StartCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
    {
        StopPreview();
        StopCamera();
        Log.d(TAG, "Surface destroyed");
        PreviewSurfaceRdy = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
