package cn.timebather.create_route.content.train.devices.controller.api;

import cn.timebather.create_route.content.train.devices.common.networking.DevicePeer;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.packets.SimpleDevicePacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public class TrainControllerClient implements DevicePeer {

    private final TrainDevice device;

    public TrainControllerClient(TrainDevice device){
        this.device = device;
    }

    @Override
    public void receive(CompoundTag tag, SimpleDevicePacketSender sender) {
        Minecraft.getInstance().player.displayClientMessage(Component.literal("Received:"+tag.getString("Text")),false);
    }
}
