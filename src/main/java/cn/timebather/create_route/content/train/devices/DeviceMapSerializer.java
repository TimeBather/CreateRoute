package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.content.train.AllTrainDevices;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeviceMapSerializer {
    public static CompoundTag serialize(Map<UUID,TrainDevice> deviceMap){
        ListTag devicesTag = new ListTag();
        deviceMap.forEach((device,instance)->{
            ResourceLocation location = AllTrainDevices.REGISTRY.get().getKey(instance.getType());
            if(location == null)
                return;
            CompoundTag tag = instance.write();
            CompoundTag deviceTag = new CompoundTag();
            if(tag != null)
                deviceTag.put("Config",tag);
            deviceTag.putString("Name",location.toString());
            devicesTag.add(deviceTag);
        });

        CompoundTag deviceManagerData = new CompoundTag();
        deviceManagerData.put("Devices",devicesTag);
        return deviceManagerData;
    }

    public static Map<UUID,TrainDevice> deserialize(CompoundTag tag){
        HashMap<UUID,TrainDevice> map = new HashMap<>();
        if(!tag.contains("Devices"))
            return map;
        ListTag list = tag.getList("Devices",CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag device = list.getCompound(i);
            ResourceLocation location = ResourceLocation.tryParse(device.getString("Name"));
            if(location == null)
                continue;
            TrainDeviceType<?> trainDeviceType = AllTrainDevices.REGISTRY.get().getValue(location);
            if(trainDeviceType == null)
                continue;
            TrainDevice trainDevice = trainDeviceType.create();
            if(device.contains("Config")){
                trainDevice.read(device.getCompound("Config"));
            }
            map.put(trainDevice.id,trainDevice);
        }
        return map;
    }
}
