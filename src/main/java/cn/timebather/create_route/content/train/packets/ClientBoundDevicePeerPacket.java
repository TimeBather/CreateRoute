package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class ClientBoundDevicePeerPacket extends TrainDevicePeerPacket{
    public ClientBoundDevicePeerPacket(FriendlyByteBuf byteBuf) {
        super(byteBuf);
    }

    public ClientBoundDevicePeerPacket(UUID trainId, int carriageId, UUID deviceId, CompoundTag packet) {
        super(trainId, carriageId, deviceId, packet);
    }

    ServerPlayer player;

    @Override
    void receive(TrainDevice device, CompoundTag packet, Player player) {
        this.player = (ServerPlayer) player;
    }

    @Override
    public void send(CompoundTag packet) {
        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(()->player),new ServerBoundDevicePeerPacket(trainId,carriageId,deviceId,packet));
    }
}
