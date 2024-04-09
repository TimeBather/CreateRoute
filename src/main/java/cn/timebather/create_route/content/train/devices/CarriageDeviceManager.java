package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.content.train.AllTrainDevices;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.UUID;

public class CarriageDeviceManager  {
    HashMap<UUID,TrainDevice> devices = new HashMap<>();

    public void attemptTransferFrom(ContraptionDeviceManager route$getDeviceManager) {
        route$getDeviceManager.devices.forEach((type,device)->{
            if(devices.containsKey(device.id))
                return;
            devices.put(device.id,device);
        });
        route$getDeviceManager.devices.clear();
    }


    public void read(CompoundTag tag){
        this.devices.putAll(DeviceMapSerializer.deserialize(tag));
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
        this.devices.put(deviceId,device);
    }
}
