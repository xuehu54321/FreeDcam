package com.troop.freedcam.themenubia.shutter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.troop.freedcam.i_camera.modules.AbstractModuleHandler;
import com.troop.freedcam.themenubia.R;
import com.troop.freedcam.ui.AppSettingsManager;
import com.troop.freedcam.ui.menu.themes.classic.shutter.FlashSwitchHandler;

/**
 * Created by troop on 12.03.2015.
 */
public class NubiaFlashSwitch extends FlashSwitchHandler
{
    LinearLayout HouseFlash;

    ImageView Off;
    ImageView On;
    ImageView Auto;
    ImageView Torch;
    ImageView textView;

    public NubiaFlashSwitch(View activity, AppSettingsManager appSettingsManager, Fragment fragment)
    {
        super(activity, appSettingsManager, fragment);
    }

    @Override
    protected void init()
    {
        textView = (ImageView)activity.findViewById(R.id.imageViewFlash);
        textView.setOnClickListener(this);

        HouseFlash = (LinearLayout)activity.findViewById(R.id.scrollViewFlash);



        Off = (ImageView)activity.findViewById(R.id.btnFlash_off);



        On = (ImageView)activity.findViewById(R.id.btnFlash_on);



        Auto = (ImageView)activity.findViewById(R.id.btnFlash_auto);



        Torch =(ImageView)activity.findViewById(R.id.btnFlash_torch);

        HouseFlash.setVisibility(View.GONE);
    }

    ImageView.OnClickListener OnView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //  LongEx.startAnimation(out);
            // HDR.startAnimation(out);
            // Movie.startAnimation(out);
            // Picture.startAnimation(out);
            cameraUiWrapper.camParametersHandler.FlashMode.SetValue("on", true);
            appSettingsManager.setString(AppSettingsManager.SETTING_FLASHMODE, "on");

            //moduleView.setBackground(activity.findViewById(R.drawable.nubia_ui_mode_pic));
            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_on);
            textView.setImageBitmap(tmp);


            HouseFlash.setVisibility(View.GONE);

        }
    };

    ImageView.OnClickListener OffView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // LongEx.startAnimation(out);
            //  HDR.startAnimation(out);
            //  Movie.startAnimation(out);
            //  Picture.startAnimation(out);
            cameraUiWrapper.camParametersHandler.FlashMode.SetValue("off", true);
            appSettingsManager.setString(AppSettingsManager.SETTING_FLASHMODE, "off");

            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_off);
            textView.setImageBitmap(tmp);


            HouseFlash.setVisibility(View.GONE);

        }
    };

    ImageView.OnClickListener TorchView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // LongEx.startAnimation(out);
            // HDR.startAnimation(out);
            // Movie.startAnimation(out);
            // Picture.startAnimation(out);
            cameraUiWrapper.camParametersHandler.FlashMode.SetValue("torch", true);
            appSettingsManager.setString(AppSettingsManager.SETTING_FLASHMODE, "torch");

            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_torch);
            textView.setImageBitmap(tmp);


            HouseFlash.setVisibility(View.GONE);

        }
    };

    ImageView.OnClickListener AutoView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // LongEx.startAnimation(out);
            // HDR.startAnimation(out);
            //.startAnimation(out);
            // Picture.startAnimation(out);
            cameraUiWrapper.camParametersHandler.FlashMode.SetValue("auto", true);
            appSettingsManager.setString(AppSettingsManager.SETTING_FLASHMODE, "auto");

            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_auto);
            textView.setImageBitmap(tmp);

            HouseFlash.setVisibility(View.GONE);

        }
    };

    @Override
    public void onClick(View v)
    {
        if (!cameraUiWrapper.moduleHandler.GetCurrentModuleName().equals(AbstractModuleHandler.MODULE_VIDEO))
        {
            if (HouseFlash.getVisibility() == View.GONE)
            {
                On.setOnClickListener(OnView);
                Off.setOnClickListener(OffView);
                Auto.setOnClickListener(AutoView);
                Torch.setOnClickListener(TorchView);

                LinearLayout Module = (LinearLayout)activity.findViewById(R.id.scrollViewModule);
                LinearLayout Night = (LinearLayout)activity.findViewById(R.id.scrollViewNight);
                Module.setVisibility(View.GONE);
                Night.setVisibility(View.GONE);

                HouseFlash.setVisibility(View.VISIBLE);
            }
            else
            {
                HouseFlash.setVisibility(View.GONE);
            }
        }
        else
        {
            if (HouseFlash.getVisibility() == View.GONE)
            {
                LinearLayout Module = (LinearLayout)activity.findViewById(R.id.scrollViewModule);
                LinearLayout Night = (LinearLayout)activity.findViewById(R.id.scrollViewNight);
                Module.setVisibility(View.GONE);
                Night.setVisibility(View.GONE);
                HouseFlash.setVisibility(View.VISIBLE);
            }
            else
            {
                HouseFlash.setVisibility(View.GONE);
            }
        }
    }

    private void initButtons()
    {
        String module =  appSettingsManager.getString(AppSettingsManager.SETTING_FLASHMODE);
        if (module.equals("auto"))
        {
            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_auto);
            textView.setImageBitmap(tmp);
        }
        else if (module.equals("on"))
        {
            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_on);
            textView.setImageBitmap(tmp);
        }
        else if (module.equals("off") ||module.equals(""))
        {
            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_off);
            textView.setImageBitmap(tmp);
        }
        else if (module.equals("torch"))
        {
            Bitmap tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.material_ui_flash_torch);
            textView.setImageBitmap(tmp);
        }
        String[] val = cameraUiWrapper.camParametersHandler.FlashMode.GetValues();
        String merge = "";
        for (String v : val)
        {
            merge += " " +v;

        }
        if (merge.contains("torch"))
            Torch.setVisibility(View.VISIBLE);
        else
            Torch.setVisibility(View.GONE);
        if (merge.contains("on"))
        {
            On.setVisibility(View.VISIBLE);
        }
        else
            On.setVisibility(View.GONE);
        if (merge.contains("off"))
            Off.setVisibility(View.VISIBLE);
        else
            Off.setVisibility(View.GONE);
        if (merge.contains("auto"))
            Auto.setVisibility(View.VISIBLE);
        else
            Auto.setVisibility(View.GONE);

    }

    @Override
    public void ParametersLoaded()
    {
        if (cameraUiWrapper == null)
            return;
        if (cameraUiWrapper.camParametersHandler.FlashMode != null)
        {
            flashmode = cameraUiWrapper.camParametersHandler.FlashMode;
            flashmode.addEventListner(this);
            //flashmode.BackgroundIsSupportedChanged(true);

        }
        Log.d(TAG, "ParametersLoaded");
        activity.post(new Runnable() {
            @Override
            public void run() {
                if (cameraUiWrapper.camParametersHandler.FlashMode != null) {
                    initButtons();
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        });

    }
}
