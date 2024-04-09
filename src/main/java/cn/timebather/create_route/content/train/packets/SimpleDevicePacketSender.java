package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.PlayerResourceManager;
import net.minecraft.nbt.CompoundTag;

public interface SimpleDevicePacketSender {
    void send(CompoundTag packet);
    PlayerResourceManager.CloseHandler onClose(PlayerResourceManager.CloseHandler closeHandler);
}