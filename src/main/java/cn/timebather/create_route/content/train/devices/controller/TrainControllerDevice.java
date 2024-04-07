package cn.timebather.create_route.content.train.devices.controller;

import cn.timebather.create_route.content.train.devices.*;
import cn.timebather.create_route.content.train.devices.controller.api.TrainControllerClient;
import cn.timebather.create_route.content.train.devices.controller.api.TrainControllerServer;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlock;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlockEntity;
import cn.timebather.create_route.interfaces.CarriageContraptionMixinInterface;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;

public class TrainControllerDevice extends TrainDevice {

    HashMap<TrainDeviceType<? extends TrainDevice>,TrainDevice> devices = new HashMap<>();

    public TrainControllerDevice(CarriageContraption contraption) {
        super(contraption);
    }

    @Override
    public void onCapture(BlockState blockState, BlockPos pos, CarriageContraption carriageContraption, BlockEntity blockEntity) {
        if(!(blockEntity instanceof TrainControllerBlockEntity trainControllerBlock)){
            return;
        }
        trainControllerBlock.getOptional().ifPresent((stackHandler)->{
            for (Item item : stackHandler.getItems()) {
                if(!(item instanceof TrainDeviceProvider trainDeviceProvider))
                    continue;
                TrainDeviceType<? extends TrainDevice> trainDeviceType = trainDeviceProvider.getDevice();
                TrainDevice device = trainDeviceType.create(carriageContraption);
                device.onCapture(blockState,pos,carriageContraption,blockEntity);
                devices.put(trainDeviceType,device);
            }
        });
        Direction facing = blockState.getValue(TrainControllerBlock.FACING);

        CarriageContraptionMixinInterface contraptionMixinInterface = (CarriageContraptionMixinInterface) this.contraption;

        Direction assemblyDirection = this.contraption.getAssemblyDirection();
        if (facing.getAxis() != assemblyDirection.getAxis()) {
            ((CarriageContraptionMixinInterface)this.contraption).createRouteRewrite$setSidewaysControl(true);
        } else {
            boolean forwards = facing == assemblyDirection;
            if (forwards) {
                ((CarriageContraptionMixinInterface)this.contraption).createRouteRewrite$setForwardControl(true);
            } else {
                ((CarriageContraptionMixinInterface)this.contraption).createRouteRewrite$setBackwardControl(true);
            }
        }
    }

    @Override
    public void assembleCheck(ContraptionDeviceManager contraptionDeviceManager) {

    }

    @Override
    public BlockState interaction(Player player, ContraptionDeviceManager deviceManager, Contraption contraption, BlockPos blockPos, BlockState blockState) {
        player.sendSystemMessage(Component.literal("HelloWorld"));
        return blockState;
    }

    Lazy<TrainControllerServer> server = Lazy.of(TrainControllerServer::new);
    Lazy<TrainControllerClient> client = Lazy.of(TrainControllerClient::new);

    @Override
    public TrainControllerServer getServer() {
        return server.get();
    }

    @Override
    public TrainControllerClient getClient() {
        return client.get();
    }

    @Override
    public void read(CompoundTag tag) {

    }

    @Override
    public CompoundTag write() {
        return null;
    }

    @Override
    public void init(ContraptionDeviceManager contraptionDeviceManager) {

    }

    @Override
    public void dispose(ContraptionDeviceManager contraptionDeviceManager) {

    }
}
