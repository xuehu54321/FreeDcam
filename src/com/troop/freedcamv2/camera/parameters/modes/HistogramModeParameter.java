package com.troop.freedcamv2.camera.parameters.modes;

import android.hardware.Camera;

import com.troop.freedcamv2.camera.parameters.I_ParameterChanged;

/**
 * Created by troop on 31.10.2014.
 */
public class HistogramModeParameter extends  BaseModeParameter {
    public HistogramModeParameter(Camera.Parameters parameters, I_ParameterChanged parameterChanged, String value, String values) {
        super(parameters, parameterChanged, value, values);
    }
}