package cn.timebather.create_route.content.train.devices.controller.api;

import cn.timebather.create_route.content.train.devices.DevicePeer;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.packets.SimpleDevicePacketSender;
import net.minecraft.nbt.CompoundTag;

public class TrainControllerServer implements DevicePeer {
    private final TrainDevice device;

    public TrainControllerServer(TrainDevice device){
        this.device = device;
    }

    @Override
    public void receive(CompoundTag tag, SimpleDevicePacketSender sender) {
        CompoundTag response = new CompoundTag();
        response.putString("Text","HelloWorld");
        sender.send(response);
    }
}
