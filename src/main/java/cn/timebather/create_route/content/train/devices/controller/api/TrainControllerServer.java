package cn.timebather.create_route.content.train.devices.controller.api;

import cn.timebather.create_route.PlayerResourceManager;
import cn.timebather.create_route.content.train.devices.DevicePeer;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import cn.timebather.create_route.content.train.packets.SimpleDevicePacketSender;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class TrainControllerServer implements DevicePeer {
    private final TrainControllerDevice device;

    public TrainControllerServer(TrainControllerDevice device){
        this.device = device;
    }

    @Override
    public void receive(CompoundTag tag, SimpleDevicePacketSender sender) {
        if(!tag.contains("DeviceId"))
            return;
        UUID deviceId = tag.getUUID("DeviceId");
        TrainDevice device = this.device.getSubDeviceById(deviceId);
        if(device == null)
            return;
        device.getServer().receive(tag.getCompound("Message"), new SimpleDevicePacketSender() {
            @Override
            public void send(CompoundTag packet) {

            }

            @Override
            public PlayerResourceManager.CloseHandler onClose(PlayerResourceManager.CloseHandler closeHandler) {
                return null;
            }
        });
    }
}
