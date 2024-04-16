package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.content.train.devices.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.SimpleDeviceGetter;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class DeviceInteractionResultPacket extends SimplePacketBase {
    UUID trainId;
    int carriageId;
    UUID deviceId;
    ResourceLocation deviceName;
    CompoundTag deviceConfig;

    public DeviceInteractionResultPacket(FriendlyByteBuf byteBuf) {
        this.trainId = byteBuf.readUUID();
        this.carriageId = byteBuf.readInt();
        this.deviceId = byteBuf.readUUID();
        this.deviceName = byteBuf.readResourceLocation();
        this.deviceConfig = byteBuf.readNbt();
    }

    public DeviceInteractionResultPacket(UUID trainId, int carriageId, UUID deviceId, ResourceLocation deviceName, CompoundTag deviceConfig){
        this.trainId = trainId;
        this.carriageId = carriageId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceConfig = deviceConfig;
    }

    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(trainId);
        friendlyByteBuf.writeInt(carriageId);
        friendlyByteBuf.writeUUID(deviceId);
        friendlyByteBuf.writeResourceLocation(deviceName);
        friendlyByteBuf.writeNbt(deviceConfig);
    }

    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(()->{
            Carriage carriage = SimpleDeviceGetter.getCarriage(CreateClient.RAILWAYS,trainId,carriageId);
            CarriageDeviceManager deviceManager = SimpleDeviceGetter.getManager(carriage);
            if(deviceManager == null)
                return;
            deviceManager.receive(deviceId,deviceName,deviceConfig);
            deviceManager.getDevice(deviceId).interaction(Minecraft.getInstance().player, carriage);
        });
        return true;
    }
}
