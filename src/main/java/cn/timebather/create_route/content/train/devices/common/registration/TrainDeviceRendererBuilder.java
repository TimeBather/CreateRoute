package cn.timebather.create_route.content.train.devices.common.registration;

import cn.timebather.create_route.content.train.devices.TrainDeviceRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

@FunctionalInterface
public interface TrainDeviceRendererBuilder {
    public TrainDeviceRenderer<?> create();
}
