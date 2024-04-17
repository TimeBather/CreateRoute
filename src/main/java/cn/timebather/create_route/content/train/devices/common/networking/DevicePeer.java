package cn.timebather.create_route.content.train.devices.common.networking;

import cn.timebather.create_route.content.train.packets.SimpleDevicePacketSender;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public interface DevicePeer {
    void receive(CompoundTag tag, SimpleDevicePacketSender sender);
}
