package com.troop.freedcam.i_camera.interfaces;

import com.troop.freedcam.i_camera.modules.AbstractModule;
import com.troop.freedcam.i_camera.modules.AbstractModuleHandler;

/**
 * Created by troop on 09.12.2014.
 */
public interface I_ModuleHandler
{

    public void SetModule(String name);
    public String GetCurrentModuleName();
    public AbstractModule GetCurrentModule();
    public boolean DoWork();
    public void SetWorkListner(AbstractModuleHandler.I_worker workerListner);
}
