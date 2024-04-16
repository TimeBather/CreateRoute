package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.PlayerResourceManager;
import cn.timebather.create_route.content.train.devices.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.SimpleDeviceGetter;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Objects;
import java.util.UUID;

public abstract class TrainDevicePeerPacket extends SimplePacketBase implements SimpleDevicePacketSender {

    UUID trainId;

    int carriageId;

    UUID deviceId;
    CompoundTag tag;


    protected TrainDevicePeerPacket(FriendlyByteBuf byteBuf){
        this.trainId = byteBuf.readUUID();
        this.carriageId = byteBuf.readInt();
        this.deviceId = byteBuf.readUUID();
        this.tag = byteBuf.readNbt();
    }

    public TrainDevicePeerPacket(UUID trainId, int carriageId, UUID deviceId, CompoundTag packet) {
        this.trainId = trainId;
        this.carriageId = carriageId;
        this.deviceId = deviceId;
        this.tag=packet;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(trainId);
        friendlyByteBuf.writeInt(carriageId);
        friendlyByteBuf.writeUUID(deviceId);
        friendlyByteBuf.writeNbt(tag);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(()->{
            Player player = context.getSender();
            Carriage carriage = SimpleDeviceGetter.getCarriage(this.getReceiverRailways(),trainId,carriageId);
            CarriageDeviceManager deviceManager = SimpleDeviceGetter.getManager(carriage);
            if(deviceManager == null)
                return;
            TrainDevice device = deviceManager.getDevice(deviceId);
            this.receive(device,tag,player);
        });
        return true;
    }

    protected abstract GlobalRailwayManager getReceiverRailways();

    protected abstract void receive(TrainDevice device, CompoundTag packet, Player player);

    public abstract void send(CompoundTag packet);

    public abstract PeerIdentifier getIdentifier(ResourceLocation location);

    public PlayerResourceManager.CloseHandler onClose(PlayerResourceManager.CloseHandler closeHandler){
        throw new NotImplementedException();
    }

    public static class PeerIdentifier{
        UUID target;
        UUID trainId;
        int contraptionId;
        UUID deviceId;
        String channelName;
        PeerIdentifier(UUID target,UUID trainId,int contraptionId,UUID deviceId,String channelName){
            this.target = target;
            this.trainId = trainId;
            this.contraptionId = contraptionId;
            this.deviceId = deviceId;
            this.channelName = channelName;
        }

        public static PeerIdentifier of(UUID target,UUID trainId,int contraptionId,UUID deviceId,String channelName){
            return new PeerIdentifier(target,trainId, contraptionId, deviceId, channelName);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PeerIdentifier that = (PeerIdentifier) o;
            return Objects.equals(target,that.target) &&
                    contraptionId == that.contraptionId &&
                    Objects.equals(trainId, that.trainId) &&
                    Objects.equals(deviceId, that.deviceId) &&
                    Objects.equals(channelName,that.channelName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(target, trainId, contraptionId, deviceId, channelName);
        }
    }
}
