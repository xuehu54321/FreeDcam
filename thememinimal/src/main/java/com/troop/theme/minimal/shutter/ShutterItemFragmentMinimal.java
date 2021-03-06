package com.troop.theme.minimal.shutter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.troop.freedcam.ui.menu.themes.classic.shutter.ExposureLockHandler;
import com.troop.freedcam.ui.menu.themes.classic.shutter.ShutterHandler;
import com.troop.freedcam.ui.menu.themes.classic.shutter.ShutterItemsFragments;
import com.troop.freedcam.utils.ApplicationContextProvider;
import com.troop.theme.minimal.R;


/**
 * Created by George on 3/17/2015.
 */
public class ShutterItemFragmentMinimal extends ShutterItemsFragments {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         view = inflater.inflate(R.layout.shutteritems_minimal_fragment, container, false);

        cameraSwitchHandler = new MiniamalCameraSwitchHandler(activity, appSettingsManager,view);

        shutterHandler = new ShutterHandler(view, this);

        moduleSwitchHandler = new MinimalModeSwitch(view, appSettingsManager, this);

        flashSwitchHandler = new MinimalFlashSwitch(view, appSettingsManager, this);

        nightModeSwitchHandler = new MinimalNightSwitch(view, appSettingsManager,this);

        exposureLockHandler = new ExposureLockHandler(view, appSettingsManager);
        exitButton = (TextView)view.findViewById(R.id.textView_Exit);

        if( ViewConfiguration.get(getActivity()).hasPermanentMenuKey())
        {
            exitButton.setVisibility(View.GONE);
        }
        else
        {
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    getActivity().finish();
                }
            });
        }

        setCameraUIwrapper();
        ParametersLoaded();
        fragmentloaded = true;
        Typeface font;

        font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/BRADHITC.TTF");

        /*TextView it1 = (TextView)view.findViewById(R.id.horTextItem1);
        it1.setTypeface(font);
        TextView it2 = (TextView)view.findViewById(R.id.horTextItem2);
        it2.setTypeface(font);
        TextView it3 = (TextView)view.findViewById(R.id.horTextItem3);
        it3.setTypeface(font);
        TextView it4 = (TextView)view.findViewById(R.id.horTextItem4);
        it4.setTypeface(font);
        TextView it1x = (TextView) view.findViewById(R.id.horTextItem5);
        it1x.setTypeface(font);
        TextView it2x = (TextView) view.findViewById(R.id.horTextItem6);
        it2x.setTypeface(font);
        TextView it3x = (TextView) view.findViewById(R.id.horTextItem7);
        it3x.setTypeface(font);
        TextView it4x = (TextView)view.findViewById(R.id.horTextItem8);
        it4x.setTypeface(font);*/

        /*TextView it5 = (TextView)view. findViewById(R.id.nubia1);
        it5.setTypeface(font);
        TextView it6 = (TextView)view. findViewById(R.id.nubia2);
        it6.setTypeface(font);
        TextView it7 = (TextView) view.findViewById(R.id.nubia3);
        it7.setTypeface(font); */

        TextView it5 = (TextView)view. findViewById(R.id.minimal_textView_flashSwitch);
        it5.setTypeface(font);
        TextView it6 = (TextView)view. findViewById(R.id.minimal_textView_ModuleSwitch);
        it6.setTypeface(font);
        TextView it7 = (TextView) view.findViewById(R.id.minimal_textView_nightmode);
        it7.setTypeface(font);



        return view;
    }

    public void MenuActive(boolean status)
    {
        ImageView Shut = (ImageView)view.findViewById(R.id.shutter_imageview);
        if (status)
        {
            Shut.setEnabled(false);
        }
        else
        {
            Shut.setEnabled(true);
        }


    }

    public void setFontStyle()
    {

        String theme = appSettingsManager.GetTheme();



        if (theme.equals("Minimal")) {



           /* TextView Clock = (TextView)findViewById(R.id.horTextItem1);
            TextView Battery = (TextView)findViewById(R.id.horTextItem1);
            TextView RES = (TextView)findViewById(R.id.horTextItem1);
            TextView Format = (TextView)findViewById(R.id.horTextItem1);
            TextView Storage = (TextView)findViewById(R.id.horTextItem1);
            Clock.setTypeface(font);
            Battery.setTypeface(font);
            Format.setTypeface(font);
            Storage.setTypeface(font);
            RES.setTypeface(font);*/

        }

    }


}


