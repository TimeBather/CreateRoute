package cn.timebather.create_route.content.train.devices.common.registration;

import cn.timebather.create_route.content.train.devices.common.rendering.TrainDeviceRenderer;

@FunctionalInterface
public interface TrainDeviceRendererBuilder {
    public TrainDeviceRenderer<?> create();
}
