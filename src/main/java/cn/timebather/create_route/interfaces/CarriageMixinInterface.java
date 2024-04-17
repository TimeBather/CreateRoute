package cn.timebather.create_route.interfaces;

import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;

public interface CarriageMixinInterface {
    CarriageDeviceManager createRoute$getDeviceManager();
}
