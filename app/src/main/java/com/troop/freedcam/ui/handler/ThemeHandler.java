package com.troop.freedcam.ui.handler;

import android.widget.LinearLayout;

import com.troop.freedcam.R;
import com.troop.freedcam.i_camera.AbstractCameraUiWrapper;
import com.troop.freedcam.i_camera.modules.I_ModuleEvent;
import com.troop.freedcam.themenubia.NubiaUi;
import com.troop.freedcam.ui.AppSettingsManager;
import com.troop.freedcam.ui.MainActivity_v2;
import com.troop.freedcam.ui.AbstractFragment;
import com.troop.freedcam.ui.menu.themes.classic.ClassicUi;
import com.troop.theme.ambient.AmbientUi;
import com.troop.theme.material.MaterialUi;
import com.troop.theme.minimal.MinimalUi;

/**
 * Created by Ingo on 14.03.2015.
 */
public class ThemeHandler implements I_ModuleEvent
{
    AppSettingsManager appSettingsManager;
    MainActivity_v2 activity_v2;
    LinearLayout uiLayout;
    AbstractFragment uiFragment;
    AbstractCameraUiWrapper cameraUiWrapper;

    public ThemeHandler(MainActivity_v2 activity_v2, AppSettingsManager appSettingsManager)
    {
        this.appSettingsManager = appSettingsManager;
        this.activity_v2 = activity_v2;
        uiLayout = (LinearLayout) activity_v2.findViewById(R.id.themeFragmentholder);

    }

    public void SetCameraUIWrapper(AbstractCameraUiWrapper cameraUiWrapper)
    {
        this.cameraUiWrapper = cameraUiWrapper;
    }

    public void GetThemeFragment()
    {
        String theme = appSettingsManager.GetTheme();

        if (uiFragment != null)
        {
            android.support.v4.app.FragmentTransaction transaction = activity_v2.getSupportFragmentManager().beginTransaction();
            transaction.remove(uiFragment);
            transaction.commitAllowingStateLoss();
            uiFragment.onDestroyView();

            uiFragment = null;
        }

        if (theme.equals("Ambient"))
        {
            AmbientUi ambientUi = new AmbientUi();
            ambientUi.SetStuff(appSettingsManager, activity_v2);
            ambientUi.SetCameraUIWrapper(cameraUiWrapper);
            uiFragment = ambientUi;
            inflateFragment(ambientUi);
        }
        if (theme.equals("Classic"))
        {
            ClassicUi CuiFragment = new ClassicUi();
            CuiFragment.SetStuff(appSettingsManager, activity_v2);

            CuiFragment.SetCameraUIWrapper(cameraUiWrapper);
            uiFragment = CuiFragment;
            inflateFragment(CuiFragment);
        }

        if (theme.equals("Material")) {
            MaterialUi materialUi = new MaterialUi();
            materialUi.SetStuff(appSettingsManager, activity_v2);
            materialUi.SetCameraUIWrapper(cameraUiWrapper);
            uiFragment = materialUi;
            inflateFragment(materialUi);
        }
        if (theme.equals("Minimal")) {
            MinimalUi minimalUi = new MinimalUi();
            minimalUi.SetStuff(appSettingsManager, activity_v2);
            minimalUi.SetCameraUIWrapper(cameraUiWrapper);
            uiFragment = minimalUi;
            inflateFragment(minimalUi);
        }
        if (theme.equals("Nubia")) {
            NubiaUi nubiaUi = new NubiaUi();
            nubiaUi.SetStuff(appSettingsManager, activity_v2);
            nubiaUi.SetCameraUIWrapper(cameraUiWrapper);
            uiFragment = nubiaUi;
            inflateFragment(nubiaUi);
        }
    }

    private void inflateFragment(AbstractFragment fragment)
    {
        android.support.v4.app.FragmentTransaction transaction = activity_v2.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.themeFragmentholder, fragment, "Main");
        transaction.commitAllowingStateLoss();
    }





    public void SetTheme(String theme)
    {
        GetThemeFragment();

    }

    @Override
    public String ModuleChanged(String module) {

        return null;


    }




}
