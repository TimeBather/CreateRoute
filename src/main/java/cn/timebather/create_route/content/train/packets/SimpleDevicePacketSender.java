package cn.timebather.create_route.content.train.packets;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface SimpleDevicePacketSender {
    void send(CompoundTag packet);
}