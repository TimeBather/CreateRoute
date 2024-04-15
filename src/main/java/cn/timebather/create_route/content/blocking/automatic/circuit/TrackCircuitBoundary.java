package cn.timebather.create_route.content.blocking.automatic.circuit;

import cn.timebather.create_route.CreateRoute;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
import com.simibubi.create.foundation.utility.Iterate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackCircuitBoundary extends SingleBlockEntityEdgePoint {

    public TrackCircuitBoundary(){
        this.dirty.put(false,true);
        this.dirty.put(true,true);
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public boolean canCoexistWith(EdgePointType<?> otherType, boolean front) {
        return otherType != getType();
    }

    public void setBoundary(boolean direction, UUID circuitId) {
        this.dirty.put(direction,false);
        this.sidedCircuits.put(direction,circuitId);
    }

    @Override
    public void tick(TrackGraph graph, boolean preTrains) {
        Map<UUID,TrackCircuit> circuits = CreateRoute.ROUTE_MANAGER.circuitDataManager.circuits;
        for (boolean i : Iterate.trueAndFalse){
            if(shouldUpdate(i)){
                TrackCircuitPropagator.propagate(graph,this,i);
            }
        }
    }

    Map<Boolean,Boolean> dirty = new HashMap<>();

    Map<Boolean,UUID> sidedCircuits = new HashMap<>();

    public void markDirty(boolean direction) {
        dirty.put(direction,true);
    }

    public boolean shouldUpdate(boolean direction){
        return dirty.containsKey(direction) && dirty.get(direction);
    }

    @Override
    public void onRemoved(TrackGraph graph) {
        TrackCircuitPropagator.onRemove(graph,this);
    }

    public UUID getGroupId(boolean side) {
        return sidedCircuits.get(side);
    }
}
