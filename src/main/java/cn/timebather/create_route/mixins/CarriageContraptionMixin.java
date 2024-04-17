package cn.timebather.create_route.mixins;

import cn.timebather.create_route.content.train.devices.common.manager.ContraptionDeviceManager;
import cn.timebather.create_route.interfaces.CarriageContraptionMixinInterface;
import cn.timebather.create_route.interfaces.DeviceCarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CarriageContraption.class,remap = false)
public class CarriageContraptionMixin implements DeviceCarriageContraption, CarriageContraptionMixinInterface {
    @Unique
    ContraptionDeviceManager createRoute$deviceManager = new ContraptionDeviceManager((CarriageContraption)(Object)this);

    @Inject(method = "capture", at = @At("TAIL"))
    void onCapture(Level world, BlockPos pos, CallbackInfoReturnable<Pair<StructureTemplate.StructureBlockInfo, BlockEntity>> cir){
        BlockState blockState = world.getBlockState(pos);
        BlockEntity be = world.getBlockEntity(pos);
        createRoute$deviceManager.onCapture(blockState,be,pos,(CarriageContraption)(Object)this);
    }

    @Override
    public ContraptionDeviceManager createRouteRewrite$getDeviceManager() {
        return createRoute$deviceManager;
    }

    @Inject(method = "assemble", at=@At("TAIL"))
    void onAssemble(Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        this.createRoute$deviceManager.onAssemble();
    }

    @Shadow
    private boolean forwardControls;

    @Shadow
    private boolean backwardControls;

    @Shadow private boolean sidewaysControls;

    @Override
    public ContraptionDeviceManager createRoute$getDeviceManager() {
        return createRoute$deviceManager;
    }

    @Unique
    @Override
    public void createRoute$setForwardControl(boolean isForwardControls) {
        this.forwardControls = isForwardControls;
    }

    @Override
    public void createRoute$setBackwardControl(boolean isBackwardControls) {
        this.backwardControls = isBackwardControls;
    }

    @Override
    public void createRoute$setSidewaysControl(boolean isSidewaysControls) {
        this.sidewaysControls = isSidewaysControls;
    }

    @Inject(method = "writeNBT",at=@At("TAIL"))
    void onWriteNBT(boolean spawnPacket, CallbackInfoReturnable<CompoundTag> cir){
        CompoundTag tag = cir.getReturnValue();
        tag.put("createRoute$devices",createRoute$deviceManager.write());
    }
    @Inject(method = "readNBT",at=@At("TAIL"))
    void onReadNBT(Level world, CompoundTag nbt, boolean spawnData, CallbackInfo ci){
        if(nbt.contains("createRoute$devices"))
            createRoute$deviceManager.read(nbt.getCompound("createRoute$devices"));
    }
}
