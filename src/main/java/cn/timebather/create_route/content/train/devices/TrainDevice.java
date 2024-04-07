package cn.timebather.create_route.content.train.devices;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TrainDevice{

    protected final CarriageContraption contraption;

    public TrainDevice(CarriageContraption contraption){
        this.contraption = contraption;
    }

    public abstract void onCapture(BlockState blockState, BlockPos pos, CarriageContraption carriageContraption, BlockEntity blockEntity);

    public abstract void read(CompoundTag tag);

    public abstract CompoundTag write();

    public abstract void init(ContraptionDeviceManager contraptionDeviceManager);

    public abstract void dispose(ContraptionDeviceManager contraptionDeviceManager);

    public abstract void assembleCheck(ContraptionDeviceManager contraptionDeviceManager);

    public abstract BlockState interaction(Player player, ContraptionDeviceManager deviceManager, Contraption contraption, BlockPos blockPos, BlockState blockState);

    public abstract DevicePeer getServer();

    public abstract DevicePeer getClient();
}
