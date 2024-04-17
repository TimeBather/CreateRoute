package cn.timebather.create_route.mixins;

import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;
import cn.timebather.create_route.interfaces.CarriageContraptionMixinInterface;
import cn.timebather.create_route.interfaces.CarriageMixinInterface;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Carriage.class,remap = false)
public class CarriageMixin implements CarriageMixinInterface {
    @Unique
    CarriageDeviceManager createRoute$deviceManager = new CarriageDeviceManager((Carriage) (Object)this);

    @Inject(method = "setContraption",at=@At("TAIL"))
    private void onSetContraption(Level level, CarriageContraption contraption, CallbackInfo ci){
        createRoute$deviceManager.attemptTransferFrom(((CarriageContraptionMixinInterface) contraption).createRoute$getDeviceManager());
    }

    @Inject(method = "write", at=@At("TAIL"))
    void onWriteNBT(DimensionPalette palette, CallbackInfoReturnable<CompoundTag> cir ){
        CompoundTag nbt = cir.getReturnValue();
        nbt.put("createRoute$devices",createRoute$deviceManager.write());
    }

    @Inject(method = "read", at=@At("TAIL"))
    private static void onReadNBT(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions, CallbackInfoReturnable<Carriage> cir){
        Carriage carriage = cir.getReturnValue();
        if(tag.contains("createRoute$devices"))
            ((CarriageMixinInterface)carriage).createRoute$getDeviceManager().read(tag.getCompound("createRoute$devices"));
    }

    @Override
    public CarriageDeviceManager createRoute$getDeviceManager() {
        return createRoute$deviceManager;
    }
}
