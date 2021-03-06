package com.troop.freedcam.camera.modules;

import android.util.Log;

import com.troop.androiddng.RawToDng;
import com.troop.freedcam.camera.BaseCameraHolder;
import com.troop.freedcam.camera.modules.image_saver.DngSaver;
import com.troop.freedcam.camera.modules.image_saver.I_WorkeDone;
import com.troop.freedcam.camera.modules.image_saver.JpegSaver;
import com.troop.freedcam.camera.modules.image_saver.JpsSaver;
import com.troop.freedcam.camera.modules.image_saver.RawSaver;
import com.troop.freedcam.i_camera.modules.I_Callbacks;
import com.troop.freedcam.i_camera.modules.ModuleEventHandler;
import com.troop.freedcam.manager.MediaScannerManager;
import com.troop.freedcam.ui.AppSettingsManager;
import com.troop.freedcam.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;



/**
 * Created by troop on 16.08.2014.
 */
public class HdrModule extends PictureModule implements I_WorkeDone
{

    private static String TAG = "freedcam.HdrModule";

    int hdrCount = 0;
    boolean aeBrackethdr = false;
    File[] files;

    public HdrModule(BaseCameraHolder cameraHandler, AppSettingsManager Settings, ModuleEventHandler eventHandler) {
        super(cameraHandler, Settings, eventHandler);
        name = ModuleHandler.MODULE_HDR;
    }

    //I_Module START
    @Override
    public String ModuleName() {
        return name;
    }

    @Override
    public void DoWork()
    {
        if (!isWorking)
        {
            files = new File[3];
            hdrCount = 0;
            if (dngcapture && baseCameraHolder.ParameterHandler.ZSL != null && baseCameraHolder.ParameterHandler.ZSL.IsSupported() && baseCameraHolder.ParameterHandler.ZSL.GetValue().equals("on"))
            {
                baseCameraHolder.errorHandler.OnError("Error: Disable ZSL for Raw or Dng capture");
                this.isWorking = false;
                return;
            }
            startworking();
            if (!ParameterHandler.isAeBracketActive)
                takePicture();
            else
                baseCameraHolder.TakePicture(null,null, aeBracketCallback);
        }
    }

    @Override
    public String ShortName() {
        return "HDR";
    }

    @Override
    public String LongName() {
        return "HDR";
    }

    @Override
    public boolean IsWorking() {
        return isWorking;
    }

    @Override
    public void LoadNeededParameters()
    {
        startThread();
        if (ParameterHandler.AE_Bracket != null && ParameterHandler.AE_Bracket.IsSupported() && ParameterHandler.isAeBracketActive)
        {
            aeBrackethdr = true;
            ParameterHandler.AE_Bracket.SetValue("true", true);
        }
    }

    @Override
    public void UnloadNeededParameters()
    {
        stopThread();
        if (ParameterHandler.AE_Bracket != null && ParameterHandler.AE_Bracket.IsSupported())
        {
            aeBrackethdr = false;
            ParameterHandler.AE_Bracket.SetValue("false", true);
        }
    }

    //I_Module END

    protected void takePicture()
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                setExposureToCamera();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final String picFormat = baseCameraHolder.ParameterHandler.PictureFormat.GetValue();
                if (picFormat.equals("jpeg")) {
                    final JpegSaver jpegSaver = new JpegSaver(baseCameraHolder, HdrModule.this, handler,Settings.GetWriteExternal());
                    jpegSaver.TakePicture();
                } else if (!parametersHandler.isDngActive && (picFormat.contains("bayer") || picFormat.contains("raw"))) {
                    final RawSaver rawSaver = new RawSaver(baseCameraHolder, HdrModule.this, handler,Settings.GetWriteExternal());
                    rawSaver.TakePicture();
                } else if (parametersHandler.isDngActive && (picFormat.contains("bayer") || picFormat.contains("raw"))) {
                    DngSaver dngSaver = new DngSaver(baseCameraHolder, HdrModule.this, handler,Settings.GetWriteExternal());
                    dngSaver.TakePicture();
                }
            }
        });
    }

    @Override
    public void OnWorkDone(File file)
    {
        baseCameraHolder.StartPreview();
        if (hdrCount == 2)
        {
            stopworking();
        }
        else if (hdrCount < 2)
        {
            hdrCount++;
            takePicture();
        }
        MediaScannerManager.ScanMedia(Settings.context.getApplicationContext(), file);
    }

    @Override
    public void OnError(String error)
    {
        baseCameraHolder.errorHandler.OnError(error);
        stopworking();
    }

    private void setExposureToCamera()
    {
        int value = 0;

        if (hdrCount == 0)
        {
           value = parametersHandler.ManualExposure.GetMinValue();
        }
        else if (hdrCount == 1)
            value = 0;
        else if (hdrCount == 2)
            value = parametersHandler.ManualExposure.GetMaxValue();
        Log.d(TAG, "Set HDR Exposure to :" + value + "for image count " + hdrCount);
        parametersHandler.ManualExposure.SetValue(value);
        Log.d(TAG, "HDR Exposure SET");
    }

    I_Callbacks.PictureCallback aeBracketCallback = new I_Callbacks.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data) {
            final String picFormat = baseCameraHolder.ParameterHandler.PictureFormat.GetValue();
            if (picFormat.equals("jpeg")) {
                final JpegSaver jpegSaver = new JpegSaver(baseCameraHolder, aeBracketDone, handler,Settings.GetWriteExternal());
                jpegSaver.saveBytesToFile(data, new File(StringUtils.getFilePathHDR(Settings.GetWriteExternal(), jpegSaver.fileEnding, hdrCount)));
            }
            else if (picFormat.equals("jps")) {
                final JpsSaver jpsSaver = new JpsSaver(baseCameraHolder, aeBracketDone, handler,Settings.GetWriteExternal());
                jpsSaver.saveBytesToFile(data,  new File(StringUtils.getFilePathHDR(Settings.GetWriteExternal(), jpsSaver.fileEnding, hdrCount)));
            }
            else if (!parametersHandler.isDngActive && (picFormat.contains("bayer") || picFormat.contains("raw"))) {
                final RawSaver rawSaver = new RawSaver(baseCameraHolder, aeBracketDone, handler,Settings.GetWriteExternal());
                rawSaver.saveBytesToFile(data,  new File(StringUtils.getFilePathHDR(Settings.GetWriteExternal(), rawSaver.fileEnding, hdrCount)));
            } else if (parametersHandler.isDngActive && (picFormat.contains("bayer") || picFormat.contains("raw"))) {
                DngSaver dngSaver = new DngSaver(baseCameraHolder, aeBracketDone, handler,Settings.GetWriteExternal());
                dngSaver.processData(data, new File(StringUtils.getFilePathHDR(Settings.GetWriteExternal(), dngSaver.fileEnding, hdrCount)));
            }
        }
    };

    I_WorkeDone aeBracketDone = new I_WorkeDone() {
        @Override
        public void OnWorkDone(File file) {
            MediaScannerManager.ScanMedia(Settings.context.getApplicationContext(), file);
            if (hdrCount == 2) {
                stopworking();
                baseCameraHolder.StartPreview();
            }
            else if (hdrCount < 2)
                hdrCount++;
        }

        @Override
        public void OnError(String error)
        {
            baseCameraHolder.errorHandler.OnError(error);
            stopworking();
        }
    };

}
