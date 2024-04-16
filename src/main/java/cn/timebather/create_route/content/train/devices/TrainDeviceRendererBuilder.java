package cn.timebather.create_route.content.train.devices;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

@FunctionalInterface
public interface TrainDeviceRendererBuilder {
    public BlockEntityRenderer<?> create();
}
