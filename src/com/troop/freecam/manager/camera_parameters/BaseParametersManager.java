package com.troop.freecam.manager.camera_parameters;

import com.troop.freecam.camera.CameraManager;
import com.troop.freecam.interfaces.ParametersChangedInterface;
import com.troop.freecam.manager.AppSettingsManager;

/**
 * Created by troop on 26.01.14.
 */
public class BaseParametersManager implements IParametersManager
{
    public enum enumParameters
    {
        All,
        Denoise,
        ManualBrightness,
        AfPriority,
        VideoModes,
        ZeroShutterLag,
        ManualWhiteBalance,
        Iso,
        ExposureMode,
        Scene,
        Ipp,
        PreviewFormat,
        PreviewSize,
        PreviewFps,
        ManualSharpness,
        ManualSaturation,
        ManualExposure,
        ManualContrast,
        ManualFocus,
        ManualShutter,
        WhiteBalanceMode,
        FlashMode,
        PictureSize,
        FocusMode,
        AntiBanding,
        LensShade,
        PictureFormat,
        ExynosRaw,
        Tripod,
        Color,
    }

    protected CameraManager cameraManager;
    protected android.hardware.Camera.Parameters parameters;
    protected AppSettingsManager preferences;
    private ParametersChangedInterface parametersChanged;
    protected boolean loadingParametersFinish = false;

    final static String TAG = "freecam.ParametersManager";

    public BaseParametersManager(CameraManager cameraManager, AppSettingsManager preferences) {
        this.cameraManager = cameraManager;
        this.preferences = preferences;
    }

    public void SetCameraParameters(android.hardware.Camera.Parameters parameters)
    {
        this.parameters = parameters;
    }

    protected void loadDefaultOrLastSavedSettings()
    {

    }

    public void setParametersChanged(ParametersChangedInterface parametersChangedInterface)
    {
        this.parametersChanged = parametersChangedInterface;
    }

    protected void onParametersCHanged(enumParameters paras)
    {
        if (parametersChanged != null && loadingParametersFinish && parameters != null)
            parametersChanged.parametersHasChanged(false, paras);
    }

    protected void onParametersCHanged(boolean reloadGui, enumParameters paras)
    {
        if (parametersChanged != null && loadingParametersFinish && parameters != null)
            parametersChanged.parametersHasChanged(reloadGui, paras);
    }

    public void UpdateGui(boolean reloadGui, enumParameters paras)
    {
         parametersChanged.parametersHasChanged(reloadGui, paras);
    }
}