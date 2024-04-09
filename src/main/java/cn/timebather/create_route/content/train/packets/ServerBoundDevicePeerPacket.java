package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.PlayerResourceManager;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class ServerBoundDevicePeerPacket extends TrainDevicePeerPacket{
    private ServerPlayer player;

    public ServerBoundDevicePeerPacket(FriendlyByteBuf byteBuf) {
        super(byteBuf);
    }

    public ServerBoundDevicePeerPacket(UUID trainId, int carriageId, UUID deviceId, CompoundTag packet){
        super(trainId,carriageId,deviceId,packet);
    }

    public static ServerBoundDevicePeerPacket create(Carriage carriage, UUID deviceId, CompoundTag packet){
        int carriageId = carriage.train.carriages.indexOf(carriage);
        UUID trainId = carriage.train.id;
        return new ServerBoundDevicePeerPacket(trainId,carriageId,deviceId,packet);
    }

    @Override
    void receive(TrainDevice device, CompoundTag packet, Player player) {
        this.player = (ServerPlayer) player;
        device.getServer().receive(packet, this);
    }

    @Override
    public void send(CompoundTag packet) {
        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(()->player),new ClientBoundDevicePeerPacket(trainId,carriageId,deviceId,packet));
    }

    public PlayerResourceManager.CloseHandler onClose(PlayerResourceManager.CloseHandler closeHandler){
        CreateRoute.PLAYER_RESOURCE_MANAGER.register(player,closeHandler);
        return ()->{
            if(!CreateRoute.PLAYER_RESOURCE_MANAGER.has(player,closeHandler)){
                return;
            }
            CreateRoute.PLAYER_RESOURCE_MANAGER.unregister(player,closeHandler);
            closeHandler.close();
        };
    }

    @Override
    public PeerIdentifier getIdentifier(ResourceLocation location) {
        return PeerIdentifier.of(player.getUUID(),trainId,carriageId,deviceId,location.toString());
    }
}
