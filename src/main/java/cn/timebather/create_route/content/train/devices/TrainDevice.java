package cn.timebather.create_route.content.train.devices;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class TrainDevice{

    @Unique
    final RandomSource randomSource = RandomSource.create();

    public UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
    protected BiConsumer<UUID, CompoundTag> packetSender;

    public void onCapture(BlockState blockState, BlockPos pos, CarriageContraption carriageContraption, BlockEntity blockEntity){
        this.id = Mth.createInsecureUUID(randomSource);
    }

    public void read(CompoundTag tag){
        id = tag.getUUID("Id");
    }

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id",id);
        return tag;
    }

    public void init(CarriageDeviceManager carriageDeviceManager){

    }

    public void assembleCheck(ContraptionDeviceManager contraptionDeviceManager){

    }

    public void interaction(Player player, Carriage carriage){
    }

    public abstract DevicePeer getServer();

    public abstract DevicePeer getClient();

    public abstract TrainDeviceType<? extends TrainDevice> getType();

    public void tick() {

    }

    public void setClientPacketSender(BiConsumer<UUID,CompoundTag> packetSender){
        this.packetSender = packetSender;
    }
}
