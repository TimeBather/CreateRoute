package cn.timebather.create_route.content.train.devices.controller.blocks;

import cn.timebather.create_route.AllMenuTypes;
import cn.timebather.create_route.content.train.devices.controller.screens.TrainControllerStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TrainControllerBlockEntity extends BlockEntity implements MenuProvider, GeoBlockEntity, GeoAnimatable {
    private final TrainControllerBlockItemStackHandler inventory = new TrainControllerBlockItemStackHandler(this);

    private final LazyOptional<TrainControllerBlockItemStackHandler> optionalInventory = LazyOptional.of(()->inventory);
    public TrainControllerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return this.optionalInventory.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        this.optionalInventory.invalidate();
        super.invalidateCaps();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.put("DeviceInventory",this.inventory.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("DeviceInventory"));
    }

    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player){
        return new TrainControllerStorageMenu(AllMenuTypes.TRAIN_CONTROLLER_STORAGE.get(),id,inventory,this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("create_route.train_controller_device");
    }

    public LazyOptional<TrainControllerBlockItemStackHandler> getOptional() {
        return optionalInventory;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void dispatchContentChanged() {
        if(level!= null && !level.isClientSide){
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        dispatchContentChanged();
    }
}
