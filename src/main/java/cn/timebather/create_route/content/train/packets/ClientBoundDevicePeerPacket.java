package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.Constants;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ClientBoundDevicePeerPacket extends TrainDevicePeerPacket{
    public ClientBoundDevicePeerPacket(FriendlyByteBuf byteBuf) {
        super(byteBuf);
    }

    public ClientBoundDevicePeerPacket(UUID trainId, int carriageId, UUID deviceId, CompoundTag packet) {
        super(trainId, carriageId, deviceId, packet);
    }

    @Override
    protected void receive(TrainDevice device, CompoundTag packet, Player player) {
        device.getClient().receive(tag,this);
    }

    @Override
    public void send(CompoundTag packet) {
        AllPackets.getChannel().sendToServer(new ClientBoundDevicePeerPacket(trainId,carriageId,deviceId,packet));
    }

    @Override
    public PeerIdentifier getIdentifier(ResourceLocation location) {
        return PeerIdentifier.of(Constants.UUID_ZERO,trainId,carriageId,deviceId,location.toString());
    }
}
