package cn.timebather.create_route.content.blocking.automatic.circuit;

import cn.timebather.create_route.AllEdgeTypes;
import cn.timebather.create_route.interfaces.TrackCircuitProvider;
import com.simibubi.create.content.trains.graph.EdgeData;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;

import java.util.UUID;

public class TrackCircuitHelper {
    public static UUID getCircuitAtPosition(TrackGraph graph,TrackEdge edge,EdgeData edgeData, double position){
        TrackCircuitProvider tcp = (TrackCircuitProvider) edgeData;
        if(!tcp.createRoute$hasTrackCircuitBoundary()){
            return getEffectiveCircuit(graph,edgeData);
        }
        TrackCircuitBoundary firstCircuit = edgeData.next(AllEdgeTypes.TRACK_CIRCUIT_BOUNDARY,0);
        if(firstCircuit == null){
            return null;
        }
        UUID current = firstCircuit.getGroupId(false);

        for (TrackEdgePoint trackEdgePoint : edgeData.getPoints()) {
            if (!(trackEdgePoint instanceof TrackCircuitBoundary tcb))
                continue;
            if (tcb.getLocationOn(edge) >= position)
                return current;
            current = tcb.getGroupId(true);
        }
        return current;
    }

    public static UUID getEffectiveCircuit(TrackGraph graph,EdgeData edgeData){
        TrackCircuitProvider tcp = (TrackCircuitProvider) edgeData;
        return !tcp.createRoute$hasTrackCircuitBoundary() ? tcp.createRoute$getTrackCircuitId() : (
                tcp.createRoute$getTrackCircuitId() == EdgeData.passiveGroup ?
                        graph.id : tcp.createRoute$getTrackCircuitId()
                );
    }
}
