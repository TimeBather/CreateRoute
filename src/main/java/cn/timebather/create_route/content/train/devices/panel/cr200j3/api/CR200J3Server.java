package cn.timebather.create_route.content.train.devices.panel.cr200j3.api;

import cn.timebather.create_route.content.train.devices.common.networking.DevicePeer;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3PanelDevice;
import cn.timebather.create_route.content.train.packets.SimpleDevicePacketSender;
import net.minecraft.nbt.CompoundTag;

public class CR200J3Server implements DevicePeer {
    private final CR200J3PanelDevice device;

    public CR200J3Server(CR200J3PanelDevice cr200J3PanelDevice) {
        this.device = cr200J3PanelDevice;
    }

    @Override
    public void receive(CompoundTag tag, SimpleDevicePacketSender sender) {
        if(!tag.contains("Operation"))
            return;
        switch (tag.getString("Operation")){
            case "SetPower":
                this.device.setPowerServer(tag.getInt("Power"));
                return;

            case "SetAirBreak":
                this.device.setAirBreakServer(tag.getInt("AirBreak"));
                return;
        }
    }
}
