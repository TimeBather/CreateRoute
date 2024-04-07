package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.content.train.AllTrainDevices;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class ContraptionDeviceManager {

    private final CarriageContraption contraption;

    public ContraptionDeviceManager(CarriageContraption contraption){
        this.contraption = contraption;
    }

    protected HashMap<TrainDeviceType<?>,TrainDevice> devices = new HashMap<>();

    public void onCapture(BlockState blockState, BlockEntity blockEntity, BlockPos pos, CarriageContraption carriageContraption){
        if(blockState.getBlock() instanceof TrainDeviceProvider trainDeviceBlock){
            TrainDeviceType<?> trainDeviceType = trainDeviceBlock.getDevice();
            if(this.devices.containsKey(trainDeviceType)){
                // @todo: multi-device is not supported yet
                return;
            }
            TrainDevice trainDevice = trainDeviceType.create(contraption);
            trainDevice.onCapture(blockState,pos,carriageContraption,blockEntity);
            this.devices.put(trainDeviceType,trainDevice);
        }
    }

    public void onAssemble() {
        this.devices.forEach((blockPos,device)->{
            device.assembleCheck(this);
        });
    }

    public void reset(){
        this.devices.forEach((deviceType,instance)->instance.dispose(this));
        this.devices.clear();
    }

    public void read(CompoundTag tag){
        if(!tag.contains("Devices"))
            return;
        this.reset();
        ListTag list = tag.getList("Devices",CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag device = list.getCompound(i);
            ResourceLocation location = ResourceLocation.tryParse(device.getString("Name"));
            if(location == null)
                continue;
            TrainDeviceType<?> trainDeviceType = AllTrainDevices.REGISTRY.get().getValue(location);
            if(trainDeviceType == null)
                continue;
            TrainDevice trainDevice = trainDeviceType.create(contraption);
            if(device.contains("Config")){
                trainDevice.read(device.getCompound("Config"));
            }
            this.devices.put(trainDeviceType,trainDevice);
        }
    }

    public CompoundTag write(){
        ListTag devicesTag = new ListTag();
        this.devices.forEach((device,instance)->{
            ResourceLocation location = AllTrainDevices.REGISTRY.get().getKey(device);
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
}
