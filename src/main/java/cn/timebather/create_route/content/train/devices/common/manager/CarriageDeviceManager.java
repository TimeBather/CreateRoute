package cn.timebather.create_route.content.train.devices.common.manager;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.packets.ServerBoundDevicePeerPacket;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class CarriageDeviceManager  {

    private final Carriage carriage;

    public CarriageDeviceManager(Carriage carriage){
        this.carriage = carriage;
    }

    HashMap<UUID, TrainDevice> devices = new HashMap<>();

    public void attemptTransferFrom(ContraptionDeviceManager route$getDeviceManager) {
        route$getDeviceManager.devices.forEach((type,device)->{
            if(devices.containsKey(device.id))
                return;
            devices.put(device.id,device);
            device.init(this);
            device.setClientPacketSender(this::sendClientSidePacket);
        });
        route$getDeviceManager.devices.clear();
    }


    public void read(CompoundTag tag) {
        this.devices.clear();
        this.devices.putAll(DeviceMapSerializer.deserialize(tag));
        this.devices.forEach((id,device)->{
            device.init(this);
            device.setClientPacketSender(this::sendClientSidePacket);
        });
    }

    public CompoundTag write(){
        return DeviceMapSerializer.serialize(this.devices);
    }

    public TrainDevice getDevice(UUID id) {
        return devices.get(id);
    }

    public void receive(UUID deviceId, ResourceLocation deviceName, CompoundTag deviceConfig) {
        if(this.devices.containsKey(deviceId)){
            TrainDevice device = this.devices.get(deviceId);
            ResourceLocation remoteLocation = AllTrainDevices.REGISTRY.get().getKey(device.getType());
            if(Objects.equals(remoteLocation,deviceName)){
                CreateRoute.LOGGER.error("Failed to parse device" + deviceId.toString() + ", data conflict! Local: "+ deviceName.toString() + ", remote:" + Objects.toString(remoteLocation));
                return;
            }

            device.read(deviceConfig);
        }

        TrainDeviceType<? extends TrainDevice> deviceType = AllTrainDevices.REGISTRY.get().getValue(deviceName);

        if(deviceType == null)
            return;

        TrainDevice device = deviceType.create();
        device.read(deviceConfig);
        device.init(this);
        device.setClientPacketSender(this::sendClientSidePacket);
        this.devices.put(deviceId,device);
    }

    public Carriage getCarriage() {
        return carriage;
    }

    public void tick() {
        this.devices.forEach((id,device)->device.tick());
    }

    public void sendClientSidePacket(UUID deviceId,CompoundTag tag){
        AllPackets.getChannel().sendToServer(new ServerBoundDevicePeerPacket(carriage.train.id,carriage.train.carriages.indexOf(carriage),deviceId,tag));
    }
}
