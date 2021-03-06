package com.troop.freedcam.ui.menu.themes.classic.menu.childs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.troop.freedcam.i_camera.modules.AbstractModuleHandler;
import com.troop.freedcam.i_camera.modules.I_ModuleEvent;
import com.troop.freedcam.i_camera.parameters.AbstractModeParameter;
import com.troop.freedcam.i_camera.parameters.I_ModeParameter;
import com.troop.freedcam.sonyapi.parameters.modes.BaseModeParameterSony;
import com.troop.freedcam.ui.AppSettingsManager;
import com.troop.freedcam.ui.menu.themes.R;
import com.troop.freedcam.ui.menu.themes.classic.menu.ExpandableGroup;

import java.util.ArrayList;

/**
 * Created by troop on 18.08.2014.
 */
public class ExpandableChild extends LinearLayout implements I_ModuleEvent, AbstractModeParameter.I_ModeParameterEvent
{
    protected String Name;
    protected AbstractModeParameter parameterHolder;
    protected AppSettingsManager appSettingsManager;
    Context context;
    protected TextView nameTextView;
    protected TextView valueTextView;
    protected String settingsname;
    protected ArrayList<String> modulesToShow;
    ExpandableGroup group;
    boolean isVisible = false;


    public ExpandableChild(Context context, ExpandableGroup group, String name, AppSettingsManager appSettingsManager, String settingsname) {
        super(context);
        this.group = group;
        this.Name = name;
        this.appSettingsManager = appSettingsManager;
        this.settingsname = settingsname;
        init(context);
    }


    protected void init(Context context)
    {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflateTheme(inflater);
        nameTextView = (TextView)findViewById(R.id.tvChild);
        nameTextView.setText(Name);
        valueTextView = (TextView)findViewById(R.id.tvChildValue);
        modulesToShow = new ArrayList<String>();
    }

    protected void inflateTheme(LayoutInflater inflater)
    {
        inflater.inflate(R.layout.expandable_childs, this);
    }

    public String getName() {
        return Name;
    }

    public String Value()
    {
        if (!parameterHolder.GetValue().equals(""))
            return parameterHolder.GetValue();
        else
            return appSettingsManager.getString(settingsname);
    }
    public void setValue(String value)
    {
        valueTextView.setText(value);
        parameterHolder.SetValue(value, true);
        if (!(parameterHolder instanceof BaseModeParameterSony))
            appSettingsManager.setString(settingsname, value);
        Log.d(getTAG(), "Set " + Name + ":" + value);
    }

    public I_ModeParameter getParameterHolder(){ return parameterHolder;}
    public void setParameterHolder(AbstractModeParameter parameterHolder, ArrayList<String> modulesToShow)
    {
        this.parameterHolder = parameterHolder;
        this.modulesToShow = modulesToShow;
        if (parameterHolder != null)
            parameterHolder.addEventListner(this);


        if (parameterHolder.IsSupported())
        {
            String campara = parameterHolder.GetValue();
            if (campara != null && !campara.equals(""))
                onValueChanged(campara);
            //onIsSupportedChanged(true);
        }
    }



    @Override
    public String ModuleChanged(String module)
    {
        if(modulesToShow.contains(module) || modulesToShow.contains(AbstractModuleHandler.MODULE_ALL))
            this.setVisibility(VISIBLE);
        else
            this.setVisibility(GONE);
        return null;
    }

    protected String getTAG()
    {
        return ExpandableChild.class.getSimpleName() + " " + Name;
    }

    @Override
    public void onValueChanged(final String val)
    {
        set(val);
    }
    private void set(final String settingValue)
    {
        if (valueTextView == null)
            return;
        valueTextView.setText(settingValue);
        valueTextView.invalidate();

    }

    @Override
    public void onIsSupportedChanged(final boolean isSupported)
    {
        if (isSupported && !isVisible) {
            isVisible = true;
            ExpandableChild.this.setVisibility(VISIBLE);
        } else if (!isSupported && isVisible) {
            isVisible = false;
            ExpandableChild.this.setVisibility(GONE);
        }
    }

    @Override
    public void onIsSetSupportedChanged(boolean isSupported)
    {

    }

    @Override
    public void onValuesChanged(String[] values) {

    }
}
