package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.common.manager.ContraptionDeviceManager;
import cn.timebather.create_route.content.train.devices.common.networking.DevicePeer;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class TrainDevice implements GeoAnimatable {

    @Unique
    final RandomSource randomSource = RandomSource.create();

    public UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
    protected BiConsumer<UUID, CompoundTag> packetSender;

    protected BlockState state = null;

    public void onCapture(BlockState blockState, BlockPos pos, CarriageContraption carriageContraption, BlockEntity blockEntity){
        this.id = Mth.createInsecureUUID(randomSource);
        this.blockPos = pos.subtract(carriageContraption.anchor);
        this.facing = blockState.hasProperty(DirectionalBlock.FACING) ? (Direction)blockState.getValue(DirectionalBlock.FACING) : Direction.NORTH;
    }

    public void read(CompoundTag tag){
        id = tag.getUUID("Id");
        if(tag.contains("BlockPos")){
            CompoundTag blockPosTag = tag.getCompound("BlockPos");
            this.blockPos = new BlockPos(
                    blockPosTag.getInt("X"),
                    blockPosTag.getInt("Y"),
                    blockPosTag.getInt("Z")
            );
            this.facing = Direction.from3DDataValue(blockPosTag.getInt("Facing"));
        }
    }

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        CompoundTag blockPosTag = new CompoundTag();
        blockPosTag.putInt("X",this.blockPos.getX());
        blockPosTag.putInt("Y",this.blockPos.getY());
        blockPosTag.putInt("Z",this.blockPos.getZ());
        blockPosTag.putInt("Facing",facing.get3DDataValue());
        tag.putUUID("Id",id);
        tag.put("BlockPos",blockPosTag);
        return tag;
    }

    Carriage carriage;

    public void init(CarriageDeviceManager carriageDeviceManager){
        this.carriage = carriageDeviceManager.getCarriage();
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

    @Override
    public double getTick(Object o) {
        return RenderUtils.getCurrentTick();
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    BlockPos blockPos = BlockPos.ZERO;

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Direction facing = Direction.NORTH;

    public Direction getFacing(){
        return facing;
    }
}
