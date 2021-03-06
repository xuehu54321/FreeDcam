package com.troop.freedcam.ui.menu.themes.classic.menu.childs;

import android.content.Context;
import android.util.Log;

import com.troop.freedcam.i_camera.parameters.AbstractModeParameter;
import com.troop.freedcam.i_camera.parameters.I_ModeParameter;
import com.troop.freedcam.sonyapi.parameters.modes.BaseModeParameterSony;
import com.troop.freedcam.ui.AppSettingsManager;
import com.troop.freedcam.ui.menu.themes.classic.menu.ExpandableGroup;

import java.util.ArrayList;

/**
 * Created by troop on 20.10.2014.
 */
public class PictureFormatExpandableChild extends ExpandableChild
{
    public I_VideoProfile PictureFormatChangedHandler;
    public PictureFormatExpandableChild(Context context, ExpandableGroup group, String name, AppSettingsManager appSettingsManager, String settingsname) {
        super(context, group, name, appSettingsManager, settingsname);
    }


    @Override
    public void setValue(String value)
    {
        if (!(parameterHolder instanceof BaseModeParameterSony))
            appSettingsManager.setString(settingsname, value);
        parameterHolder.SetValue(value, true);
        valueTextView.setText(value);
        Log.d(getTAG(), "Set " + Name + ":" + value);
        if (PictureFormatChangedHandler != null)
            PictureFormatChangedHandler.VideoProfileChanged(value);
    }

    @Override
    public String Value() {
        return super.Value();
    }

    @Override
    public I_ModeParameter getParameterHolder() {
        return super.getParameterHolder();
    }


    public void setParameterHolder(AbstractModeParameter parameterHolder, ArrayList<String> modulesToShow) {
       super.setParameterHolder(parameterHolder,modulesToShow);
       if (PictureFormatChangedHandler != null) {
           String campara = parameterHolder.GetValue();
           if (campara.equals(""))
               campara = appSettingsManager.getString(AppSettingsManager.SETTING_PICTUREFORMAT);
           if (campara.equals(""))
               return;
           onValueChanged(campara);
           PictureFormatChangedHandler.VideoProfileChanged(campara);
       }

    }

    @Override
    public void onValueChanged(String val)
    {
        super.onValueChanged(val);
    }
}
