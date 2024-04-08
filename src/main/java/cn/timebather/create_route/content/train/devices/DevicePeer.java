package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.packets.SimpleDevicePacketSender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public interface DevicePeer {
    void receive(CompoundTag tag, SimpleDevicePacketSender sender);
}
