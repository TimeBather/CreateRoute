package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ServerBoundDevicePeerPacket extends TrainDevicePeerPacket{
    public ServerBoundDevicePeerPacket(FriendlyByteBuf byteBuf) {
        super(byteBuf);
    }

    public ServerBoundDevicePeerPacket(UUID trainId, int carriageId, UUID deviceId, CompoundTag packet){
        super(trainId,carriageId,deviceId,packet);
    }

    public ServerBoundDevicePeerPacket create(CarriageContraption contraption, UUID deviceId, CompoundTag packet){
        CarriageContraptionEntity entity = ((CarriageContraptionEntity) contraption.entity);
        Carriage carriage = entity.getCarriage();
        int carriageId = carriage.train.carriages.indexOf(carriage);
        UUID trainId = carriage.train.id;
        return new ServerBoundDevicePeerPacket(trainId,carriageId,deviceId,packet);
    }

    @Override
    void receive(TrainDevice device, CompoundTag packet, Player player) {
        device.getServer().receive(packet, this);
    }

    @Override
    public void send(CompoundTag packet) {
        AllPackets.getChannel().sendToServer(new ClientBoundDevicePeerPacket(trainId,carriageId,deviceId,packet));
    }
}
