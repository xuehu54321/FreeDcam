package com.troop.freedcam.i_camera.parameters;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ingo on 26.12.2014.
 *
 * This lowlevel class is implemented for all apis as wrapper for their parameters
 * only this class should used by the ui
 */
public class AbstractModeParameter implements I_ModeParameter
{
    //Holds the ui thread to invoke ui items
    Handler uihandler;
    private static String TAG = AbstractModeParameter.class.getSimpleName();

    public AbstractModeParameter(Handler uiHandler)
    {
        events = new ArrayList<I_ModeParameterEvent>();
        this.uihandler = uiHandler;
    }

    /**
     * the Interface that must be included to listen on the background changed events
     * The ui should listen to that events to show dynamic background changes
     */
    public interface I_ModeParameterEvent
    {
        void onValueChanged(String val);
        void onIsSupportedChanged(boolean isSupported);
        void onIsSetSupportedChanged(boolean isSupported);
        void onValuesChanged(String[] values);
    }

    private List<I_ModeParameterEvent> events;

    public void addEventListner(I_ModeParameterEvent eventListner)
    {
        if (!events.contains(eventListner))
            events.add(eventListner);
    }
    public void removeEventListner(I_ModeParameterEvent parameterEvent)
    {
        if (events.contains(parameterEvent))
            events.remove(parameterEvent);
    }

    /***
     *
     * @return true if the parameter is supported
     */
    @Override
    public boolean IsSupported() {
        return false;
    }

    /**
     *
     * @param valueToSet to the camera
     * @param setToCamera not needed anymore
     */
    @Override
    public void SetValue(String valueToSet, boolean setToCamera) {

    }

    @Override
    public String GetValue() {
        return null;
    }

    @Override
    public String[] GetValues() {
        return new String[0];
    }

    /**
     * Throws the event to all registerd listners that the value has changed
     * @param value
     */
    public void BackgroundValueHasChanged(final String value)
    {
        if (events == null || events.size() == 0 || value.equals(""))
            return;
        //Log.d(TAG, "BackgroundValueHasCHanged:" + value);
        uihandler.post(new Runnable() {
            @Override
            public void run() {
                for (int i= 0; i< events.size(); i ++)
                {
                    if (events.get(i) == null)
                    {
                        events.remove(i);
                        i--;

                    }
                    else
                        events.get(i).onValueChanged(value);
                }
            }
        });


    }
    public void BackgroundValuesHasChanged(final String[] value)
    {
        uihandler.post(new Runnable() {
            @Override
            public void run() {
                for (int i= 0; i< events.size(); i ++)
                {
                    if (events.get(i) == null)
                    {
                        events.remove(i);
                        i--;

                    }
                    else
                        events.get(i).onValuesChanged(value);
                }
            }
        });

    }

    /**
     *
     * @param value if true set parameter is supported, else not
     */
    public void BackgroundIsSupportedChanged(final boolean value)
    {
        uihandler.post(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG, "BackgroundSupportedCHanged:" + value);
                for (int i= 0; i< events.size(); i ++)
                {
                    if (events.get(i) == null)
                    {
                        events.remove(i);
                        i--;

                    }
                    else
                        events.get(i).onIsSupportedChanged(value);
                }
            }
        });

    }

    /**
     *
     * @param value if true the value can set to the camera, if false the value is read only
     * this affects only sonyapi and camera2
     */
    public void BackgroundSetIsSupportedHasChanged(final boolean value)
    {
        uihandler.post(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG, "BackgroundSetSupportedCHanged:" + value);
                for (int i= 0; i< events.size(); i ++)
                {
                    if (events.get(i) == null)
                    {
                        events.remove(i);
                        i--;

                    }
                    else
                        events.get(i).onIsSetSupportedChanged(value);
                }
            }
        });

    }
}
