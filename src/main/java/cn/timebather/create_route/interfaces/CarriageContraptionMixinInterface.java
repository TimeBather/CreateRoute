package cn.timebather.create_route.interfaces;

import cn.timebather.create_route.content.train.devices.common.manager.ContraptionDeviceManager;

public interface CarriageContraptionMixinInterface {

    public ContraptionDeviceManager createRoute$getDeviceManager();
    public void createRoute$setForwardControl(boolean isForwardControls);

    public void createRoute$setBackwardControl(boolean isBackwardControls);
    public void createRoute$setSidewaysControl(boolean isSidewaysControls);
}
