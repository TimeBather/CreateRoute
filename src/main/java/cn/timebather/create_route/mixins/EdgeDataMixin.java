package cn.timebather.create_route.mixins;

import cn.timebather.create_route.AllEdgeTypes;
import cn.timebather.create_route.interfaces.TrackCircuitProvider;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.EdgeData;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

@Mixin(value = EdgeData.class,remap = false)
public class EdgeDataMixin implements TrackCircuitProvider {

    @Shadow private List<TrackEdgePoint> points;
    @Shadow @Final public static UUID passiveGroup;
    @Unique
    private UUID createRoute$trackCircuitId = EdgeData.passiveGroup;

    @Unique
    public boolean createRoute$hasTrackCircuitBoundary() {
        return createRoute$trackCircuitId == null;
    }

    @Unique
    public void createRoute$setTrackCircuitId(UUID id) {
        this.createRoute$trackCircuitId = id;
    }

    @Unique
    public UUID createRoute$getTrackCircuitId() {
        return createRoute$trackCircuitId;
    }

    @Inject(method = "write",at=@At("TAIL"))
    void onWrite(DimensionPalette dimensions, CallbackInfoReturnable<CompoundTag> cir){
        CompoundTag tag = cir.getReturnValue();
        if(createRoute$trackCircuitId != null)
            tag.putUUID("CreateRoute$TrackCircuitId",createRoute$trackCircuitId);
    }

    @Inject(method = "read",at=@At("TAIL"))
    private static void onRead(CompoundTag nbt, TrackEdge edge, TrackGraph graph, DimensionPalette dimensions, CallbackInfoReturnable<EdgeData> cir){
        ((TrackCircuitProvider)cir.getReturnValue()).createRoute$setTrackCircuitId(nbt.contains("CreateRoute$TrackCircuitId") ? nbt.getUUID("CreateRoute$TrackCircuitId") : null);
    }

    @Inject(method = "addPoint",at=@At("HEAD"))
    private void onAddPoint(TrackGraph graph, TrackEdgePoint point, CallbackInfo ci){
        if (point.getType() == AllEdgeTypes.TRACK_CIRCUIT_BOUNDARY)
            createRoute$setTrackCircuitId(null);
    }

    @Inject(method = "removePoint",at=@At("TAIL"))
    private void onRemovePoint(TrackGraph graph, TrackEdgePoint point, CallbackInfo ci){
        EdgeData self = (EdgeData)(Object) this;
        if(point.getType() == AllEdgeTypes.TRACK_CIRCUIT_BOUNDARY)
            createRoute$setTrackCircuitId(self.next(point.getType(), 0) == null ? passiveGroup : null);
    }
}
