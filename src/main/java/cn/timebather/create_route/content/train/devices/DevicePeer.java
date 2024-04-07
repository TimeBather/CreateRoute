package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.content.train.AllTrainDevices;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public interface DevicePeer {
    void receive(CompoundTag tag);

    static void send(UUID train,TrainDeviceType<?> device,CompoundTag operation){
        ResourceLocation location = AllTrainDevices.REGISTRY.get().getKey(device);
    }
}
