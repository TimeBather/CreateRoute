package cn.timebather.create_route.content.train.devices.controller;

import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.*;
import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.common.manager.DeviceMapSerializer;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceProvider;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.devices.controller.api.TrainControllerClient;
import cn.timebather.create_route.content.train.devices.controller.api.TrainControllerServer;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlock;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlockEntity;
import cn.timebather.create_route.content.train.traction.TractionEngine;
import cn.timebather.create_route.interfaces.CarriageContraptionMixinInterface;
import cn.timebather.create_route.interfaces.TrainTractionEngineProvider;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;

public class TrainControllerDevice extends TrainDevice {

    HashMap<UUID,TrainDevice> devices = new HashMap<>();
    private CarriageDeviceManager carriageDeviceManager;

    public TrainControllerDevice() {}

    @Override
    public void onCapture(BlockState blockState, BlockPos pos, CarriageContraption carriageContraption, BlockEntity blockEntity) {
        super.onCapture(blockState,pos,carriageContraption,blockEntity);
        if(!(blockEntity instanceof TrainControllerBlockEntity trainControllerBlock)){
            return;
        }
        trainControllerBlock.getOptional().ifPresent((stackHandler)->{
            for (Item item : stackHandler.getItems()) {
                if(!(item instanceof TrainDeviceProvider trainDeviceProvider))
                    continue;
                TrainDeviceType<? extends TrainDevice> trainDeviceType = trainDeviceProvider.getDevice();
                TrainDevice device = trainDeviceType.create();
                device.onCapture(blockState,pos,carriageContraption,blockEntity);
                devices.put(device.id,device);
            }
        });
        Direction facing = blockState.getValue(TrainControllerBlock.FACING);

        CarriageContraptionMixinInterface contraptionMixinInterface = (CarriageContraptionMixinInterface) carriageContraption;

        Direction assemblyDirection = carriageContraption.getAssemblyDirection();
        if (facing.getAxis() != assemblyDirection.getAxis()) {
            contraptionMixinInterface.createRoute$setSidewaysControl(true);
        } else {
            boolean forwards = facing == assemblyDirection;
            if (forwards) {
                contraptionMixinInterface.createRoute$setForwardControl(true);
            } else {
                contraptionMixinInterface.createRoute$setBackwardControl(true);
            }
        }
    }


    Lazy<TrainControllerServer> server = Lazy.of(()-> new TrainControllerServer(this));
    Lazy<TrainControllerClient> client = Lazy.of(()-> new TrainControllerClient(this));

    @Override
    public TrainControllerServer getServer() {
        return server.get();
    }

    @Override
    public TrainControllerClient getClient() {
        return client.get();
    }

    @Override
    public TrainDeviceType<TrainControllerDevice> getType() {
        return AllTrainDevices.TRAIN_CONTROLLER.get();
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        devices.putAll(DeviceMapSerializer.deserialize(tag));
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = super.write();
        tag.put("Devices",DeviceMapSerializer.serialize(devices).getList("Devices", Tag.TAG_COMPOUND));
        return tag;
    }

    @Override
    public void init(CarriageDeviceManager carriageDeviceManager) {
        super.init(carriageDeviceManager);
        this.devices.forEach((id,dev)->{
            dev.init(carriageDeviceManager);
            dev.setClientPacketSender(this::sendClientSidePacket);
        });
        this.carriageDeviceManager = carriageDeviceManager;
    }

    @Override
    public void interaction(Player player, Carriage carriage) {
        this.getType().openScreen(this,carriage);
    }

    public Collection<TrainDevice> getSubDevices(){
        return this.devices.values();
    }

    public TrainDevice getSubDeviceById(UUID id){
        return this.devices.get(id);
    }

    @Override
    public void tick() {
        this.devices.forEach((id,device)->device.tick());

        Train train = carriageDeviceManager.getCarriage().train;
        TrainTractionEngineProvider tep = (TrainTractionEngineProvider) train;
        if(train == null)
            return;
        if(tep.getEngine() == null)
            tep.setEngine(new TractionEngine(train));
    }

    @Override
    public void setClientPacketSender(BiConsumer<UUID, CompoundTag> packetSender) {
        super.setClientPacketSender(packetSender);
    }

    public void sendClientSidePacket(UUID deviceId,CompoundTag tag){
        CompoundTag warp = new CompoundTag();
        warp.putUUID("DeviceId",deviceId);
        warp.put("Message",tag);
        this.packetSender.accept(this.id,warp);
    }
}
