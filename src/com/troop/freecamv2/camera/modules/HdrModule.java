package com.troop.freecamv2.camera.modules;

import com.troop.freecamv2.camera.BaseCameraHolder;

import com.troop.freecam.manager.SoundPlayer;
import com.troop.freecamv2.ui.AppSettingsManager;

/**
 * Created by troop on 16.08.2014.
 */
public class HdrModule extends AbstractModule {
    public HdrModule(BaseCameraHolder cameraHandler, SoundPlayer soundPlayer, AppSettingsManager Settings, ModuleEventHandler eventHandler) {
        super(cameraHandler, soundPlayer, Settings, eventHandler);
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

    }

    @Override
    public boolean IsWorking() {
        return isWorking;
    }
//I_Module END

}