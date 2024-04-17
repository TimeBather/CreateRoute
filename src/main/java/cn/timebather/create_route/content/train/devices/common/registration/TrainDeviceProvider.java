package cn.timebather.create_route.content.train.devices.common.registration;

import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;

public interface TrainDeviceProvider {
    TrainDeviceType<? extends TrainDevice> getDevice();
}
