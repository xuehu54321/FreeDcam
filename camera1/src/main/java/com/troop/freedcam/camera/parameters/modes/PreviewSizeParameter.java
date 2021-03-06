package com.troop.freedcam.camera.parameters.modes;

import android.os.Handler;

import com.troop.freedcam.camera.BaseCameraHolder;
import com.troop.freedcam.i_camera.AbstractCameraHolder;
import com.troop.freedcam.utils.DeviceUtils;

import java.util.HashMap;

/**
 * Created by troop on 21.08.2014.
 */
public class PreviewSizeParameter extends BaseModeParameter
{
    AbstractCameraHolder baseCameraHolder;

    public PreviewSizeParameter(Handler handler,HashMap<String, String> parameters, BaseCameraHolder parameterChanged, String value, String values, AbstractCameraHolder cameraHolder)
    {
        super(handler, parameters, parameterChanged, value, values);
        this.baseCameraHolder = cameraHolder;
    }

    @Override
    public void SetValue(String valueToSet, boolean setToCam)
    {
        //if (baseCameraHolder.IsPreviewRunning())
            //baseCameraHolder.StopPreview();

        if(DeviceUtils.isZTEADV())
            parameters.put(value, valueToSet);

        try
        {
            ((BaseCameraHolder)baseCameraHolder).SetPreviewSize(valueToSet);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }
        //baseCameraHolder.SetCameraParameters(parameters);
        //if (!baseCameraHolder.IsPreviewRunning())
            //baseCameraHolder.StartPreview();
    }
}
