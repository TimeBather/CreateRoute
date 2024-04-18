package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.common.networking.SimpleDeviceGetter;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class DeviceConfigureRequestPacket extends SimplePacketBase {
    UUID trainId;
    int carriageId;
    UUID deviceId;

    public DeviceConfigureRequestPacket(
            UUID trainId,
            int carriageId,
            UUID deviceId
    ){
        this.trainId = trainId;
        this.carriageId = carriageId;
        this.deviceId = deviceId;
    }

    public DeviceConfigureRequestPacket(FriendlyByteBuf friendlyByteBuf){
        this.trainId = friendlyByteBuf.readUUID();
        this.carriageId = friendlyByteBuf.readInt();
        this.deviceId = friendlyByteBuf.readUUID();
    }
    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(trainId);
        friendlyByteBuf.writeInt(carriageId);
        friendlyByteBuf.writeUUID(deviceId);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {

        context.enqueueWork(()->{
            ServerPlayer sender = context.getSender();
            Carriage carriage = SimpleDeviceGetter.getCarriage(CreateClient.RAILWAYS,trainId,carriageId);
            CarriageDeviceManager deviceManager = SimpleDeviceGetter.getManager(carriage);
            if(deviceManager == null)
                return;
            TrainDevice device = deviceManager.getDevice(deviceId);
            if(device == null)
                return;
            ResourceLocation typeLocation = AllTrainDevices.REGISTRY.get().getKey(device.getType());
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(()->sender), new DeviceConfigureResponsePacket(trainId,carriageId,deviceId,typeLocation,device.write()));
        });
        return true;
    }
}
