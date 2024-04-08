package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.content.train.AllTrainDevices;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.UUID;

public class ContraptionDeviceManager {

    private final CarriageContraption contraption;

    public ContraptionDeviceManager(CarriageContraption contraption){
        this.contraption = contraption;
    }

    protected HashMap<UUID, TrainDevice> devices = new HashMap<>();
    protected HashMap<BlockPos, UUID> locators = new HashMap<>();

    public void onCapture(BlockState blockState, BlockEntity blockEntity, BlockPos pos, CarriageContraption carriageContraption){
        if(blockState.getBlock() instanceof TrainDeviceProvider trainDeviceBlock){
            TrainDeviceType<?> trainDeviceType = trainDeviceBlock.getDevice();
            TrainDevice trainDevice = trainDeviceType.create();
            trainDevice.onCapture(blockState,pos,carriageContraption,blockEntity);
            this.devices.put(trainDevice.id,trainDevice);
            this.locators.put(pos.subtract(contraption.anchor),trainDevice.id);
        }
    }

    public void onAssemble() {
        this.devices.forEach((blockPos,device)->{
            device.assembleCheck(this);
        });
    }

    public void reset(){
        this.devices.clear();
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        ListTag locatorsTag = new ListTag();
        this.locators.forEach((pos,id)->{
            CompoundTag locator = new CompoundTag();
            locator.putLong("Locator",pos.asLong());
            locator.putUUID("Uuid",id);
            locatorsTag.add(locator);
        });
        tag.put("Locators",locatorsTag);
        return tag;
    }

    public void read(CompoundTag tag) {
        if(!tag.contains("Locators"))
            return;
        ListTag locatorsTag = tag.getList("Locators",Tag.TAG_COMPOUND);
        for (int i = 0; i < locatorsTag.size(); i++) {
            CompoundTag locator = locatorsTag.getCompound(i);
            if(!locator.contains("Locator") || !locator.contains("Uuid"))
                continue;
            this.locators.put(BlockPos.of(locator.getLong("Locator")),locator.getUUID("Uuid"));
        }
    }
}
