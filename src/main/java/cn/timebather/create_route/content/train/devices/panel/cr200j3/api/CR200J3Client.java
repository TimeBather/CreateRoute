package cn.timebather.create_route.content.train.devices.panel.cr200j3.api;

import cn.timebather.create_route.content.train.devices.common.networking.DevicePeer;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3PanelDevice;
import cn.timebather.create_route.content.train.packets.SimpleDevicePacketSender;
import net.minecraft.nbt.CompoundTag;

public class CR200J3Client implements DevicePeer {
    public CR200J3Client(CR200J3PanelDevice cr200J3PanelDevice) {

    }

    @Override
    public void receive(CompoundTag tag, SimpleDevicePacketSender sender) {

    }
}
