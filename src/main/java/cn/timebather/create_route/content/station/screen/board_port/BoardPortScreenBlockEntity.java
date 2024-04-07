package cn.timebather.create_route.content.station.screen.board_port;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BoardPortScreenBlockEntity extends BlockEntity implements GeoBlockEntity {

    String leftPortId = "";

    String rightPortId = "";

    String deviceId = "";

    public BoardPortScreenBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos(), this.getBlockPos().offset(this.getBlockState().getValue(BoardPortScreen.FACING).getNormal().multiply(8)));
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = new CompoundTag();
        updateTag.putString("Left",leftPortId);
        updateTag.putString("Right",rightPortId);
        updateTag.putString("DeviceId",deviceId);
        return updateTag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.leftPortId = tag.getString("Left");
        this.rightPortId = tag.getString("Right");
        this.deviceId = tag.getString("DeviceId");
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.leftPortId = nbt.getString("Left");
        this.rightPortId = nbt.getString("Right");
        this.deviceId = nbt.getString("DeviceId");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putString("Left",leftPortId);
        tag.putString("Right",rightPortId);
        tag.putString("DeviceId",deviceId);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.leftPortId = tag.getString("Left");
        this.rightPortId = tag.getString("Right");
        this.deviceId = tag.getString("DeviceId");
    }
}
