package cn.timebather.create_route.mixins;

import cn.timebather.create_route.content.train.traction.TractionEngine;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(value = Train.class,remap = false)
public class TrainMixin{
    @Unique
    @Nullable
    public TractionEngine createRoute$tractionEngine;

    @Shadow
    public TrackGraph graph;

    @Shadow public List<Carriage> carriages;

    @Inject(method = "tick",at=@At("HEAD"))
    private void beforeTick(Level level, CallbackInfo ci) {
        if (this.graph == null) {
            return;
        }
        if(this.createRoute$tractionEngine!=null)
            this.createRoute$tractionEngine.tick();
    }

    @Inject(method = "tickPassiveSlowdown",at=@At("HEAD"),cancellable = true)
    private void onTickPassiveSlowdown(CallbackInfo ci){
        if(this.createRoute$tractionEngine!=null){
            ci.cancel();
        }
    }

    @Inject(method = "read",at=@At("TAIL"))
    private static void onRead(CompoundTag tag, Map<UUID, TrackGraph> trackNetworks, DimensionPalette dimensions, CallbackInfoReturnable<Train> cir){
        if(tag.contains("createRouteRewrite$tractionEngine")){
            TractionEngine engine = new TractionEngine(cir.getReturnValue());
            engine.read(tag.getCompound("createRouteRewrite$tractionEngine"));
            ((TrainMixin) (Object) cir.getReturnValue())
                    .createRoute$tractionEngine = engine;
        }
    }

    @Inject(method = "write",at=@At("TAIL"))
    private void onWrite(DimensionPalette dimensions, CallbackInfoReturnable<CompoundTag> cir){
        CompoundTag tag = cir.getReturnValue();
        if(this.createRoute$tractionEngine != null)
            tag.put("createRouteRewrite$tractionEngine",this.createRoute$tractionEngine.write());
    }
}
