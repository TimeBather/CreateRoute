package cn.timebather.create_route;

import cn.timebather.create_route.content.blocking.automatic.circuit.TrackCircuitBoundary;
import com.simibubi.create.content.trains.graph.EdgePointType;

public class AllEdgeTypes {
    public static EdgePointType<TrackCircuitBoundary> TRACK_CIRCUIT_BOUNDARY = EdgePointType.register(CreateRoute.asResource("track_circuit"),TrackCircuitBoundary::new);
}
