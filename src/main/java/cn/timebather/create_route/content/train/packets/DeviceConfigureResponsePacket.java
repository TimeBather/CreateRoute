package cn.timebather.create_route.content.train.packets;

import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.common.networking.SimpleDeviceGetter;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class DeviceConfigureResponsePacket extends SimplePacketBase {
    UUID trainId;
    int carriageId;
    UUID deviceId;
    ResourceLocation deviceName;
    CompoundTag deviceConfig;

    public DeviceConfigureResponsePacket(FriendlyByteBuf byteBuf) {
        this.trainId = byteBuf.readUUID();
        this.carriageId = byteBuf.readInt();
        this.deviceId = byteBuf.readUUID();
        this.deviceName = byteBuf.readResourceLocation();
        this.deviceConfig = byteBuf.readNbt();
    }

    public DeviceConfigureResponsePacket(UUID trainId, int carriageId, UUID deviceId, ResourceLocation deviceName, CompoundTag deviceConfig){
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
            Carriage carriage = SimpleDeviceGetter.getCarriage(Create.RAILWAYS,trainId,carriageId);
            CarriageDeviceManager deviceManager = SimpleDeviceGetter.getManager(carriage);
            if(deviceManager == null)
                return;
            deviceManager.receive(deviceId,deviceName,deviceConfig);
        });
        return true;
    }
}
