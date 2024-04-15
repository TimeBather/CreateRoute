package cn.timebather.create_route.content.blocking.automatic.circuit;

import cn.timebather.create_route.AllEdgeTypes;
import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.interfaces.TrackCircuitProvider;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.graph.*;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;

import java.util.*;
import java.util.function.Predicate;

public class TrackCircuitPropagator {
    public static void propagate(TrackGraph graph,TrackCircuitBoundary boundary,boolean direction){
        TrackCircuit circuit = new TrackCircuit(UUID.randomUUID());
        UUID circuitId = circuit.id;
        boundary.setBoundary(direction,circuitId);

        walkCircuit(graph,boundary,direction, pair->{
            TrackNode node = pair.getFirst();
            TrackCircuitBoundary tcb = pair.getSecond();
            tcb.setBoundary(tcb.edgeLocation.getFirst() != node.getLocation(),circuitId);
            return true;
        },edge->{
            TrackCircuitProvider tcp = (TrackCircuitProvider)edge;
            if(!tcp.createRoute$hasTrackCircuitBoundary()){
                // Attempt to remove track circuit
                CreateRoute.ROUTE_MANAGER.circuitDataManager.removeCircuit(tcp.createRoute$getTrackCircuitId());
            }
            tcp.createRoute$setTrackCircuitId(circuitId);
            return true;
        },false);
        CreateRoute.ROUTE_MANAGER.circuitDataManager.addCircuit(circuit);
        Create.RAILWAYS.markTracksDirty();
    }

    public static void onRemove(TrackGraph graph,TrackCircuitBoundary boundary){
        for(boolean front : Iterate.trueAndFalse){
            walkCircuit(graph,boundary,front,boundaryPair->{
                TrackCircuitBoundary tcb = boundaryPair.getSecond();
                TrackNode node = boundaryPair.getFirst();
                tcb.markDirty(tcb.edgeLocation.getFirst() != node.getLocation());
                return false;
            },edgeData -> {
                TrackCircuitProvider tcp = (TrackCircuitProvider)edgeData;
                if(!tcp.createRoute$hasTrackCircuitBoundary()){
                    CreateRoute.ROUTE_MANAGER.circuitDataManager.removeCircuit(tcp.createRoute$getTrackCircuitId());
                    tcp.createRoute$setTrackCircuitId(EdgeData.passiveGroup);
                    return true;
                }
                return false;
            },false);
        }
    }

    public static void walkCircuit(TrackGraph graph, TrackCircuitBoundary circuit, boolean front,
                                   Predicate<Pair<TrackNode, TrackCircuitBoundary>> boundaryCallback, Predicate<EdgeData> nonBoundaryCallback,
                                   boolean forCollection) {

        Couple<TrackNodeLocation> edgeLocation = circuit.edgeLocation;
        Couple<TrackNode> startNodes = edgeLocation.map(graph::locateNode);
        Couple<TrackEdge> startEdges = startNodes.mapWithParams((l1, l2) -> graph.getConnectionsFrom(l1)
                .get(l2), startNodes.swap());

        TrackNode node1 = startNodes.get(front);
        TrackNode node2 = startNodes.get(!front);
        TrackEdge startEdge = startEdges.get(front);
        TrackEdge oppositeEdge = startEdges.get(!front);

        if (startEdge == null)
            return;

        if (!forCollection) {
            Create.RAILWAYS.sync.edgeDataChanged(graph, node1, node2, startEdge, oppositeEdge);
        }

        // Check for signal on the same edge

        TrackCircuitBoundary immediateBoundary = startEdge.getEdgeData()
                .next(AllEdgeTypes.TRACK_CIRCUIT_BOUNDARY, circuit.getLocationOn(startEdge));
        if (immediateBoundary != null) {
            return;
        }

        // Search for any connected signals
        List<Couple<TrackNode>> frontier = new ArrayList<>();
        frontier.add(Couple.create(node2, node1));
        walkCircuit(graph, frontier, boundaryCallback, nonBoundaryCallback, forCollection);
    }

    private static void walkCircuit(TrackGraph graph, List<Couple<TrackNode>> frontier,
                                    Predicate<Pair<TrackNode, TrackCircuitBoundary>> circuitCallback, Predicate<EdgeData> nonBoundaryCallback,
                                    boolean forCollection) {
        // This method is copied and edited from Create mod https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/content/trains/signal/SignalPropagator.java#L91
        Set<TrackEdge> visited = new HashSet<>();
        while (!frontier.isEmpty()) {
            Couple<TrackNode> couple = frontier.remove(0);
            TrackNode currentNode = couple.getFirst();
            TrackNode prevNode = couple.getSecond();

            EdgeWalk: for (Map.Entry<TrackNode, TrackEdge> entry : graph.getConnectionsFrom(currentNode)
                    .entrySet()) {
                TrackNode nextNode = entry.getKey();
                TrackEdge edge = entry.getValue();

                if (nextNode == prevNode)
                    continue;

                // already checked this edge
                if (!visited.add(edge))
                    continue;

                // chain signal: check if reachable
                if (forCollection && !graph.getConnectionsFrom(prevNode)
                        .get(currentNode)
                        .canTravelTo(edge))
                    continue;

                TrackEdge oppositeEdge = graph.getConnectionsFrom(nextNode)
                        .get(currentNode);
                visited.add(oppositeEdge);

                for (boolean flip : Iterate.falseAndTrue) {
                    TrackEdge currentEdge = flip ? oppositeEdge : edge;
                    EdgeData signalData = currentEdge.getEdgeData();

                    // no boundary- update group of edge
                    if (!((TrackCircuitProvider)signalData).createRoute$hasTrackCircuitBoundary()) {
                        if (nonBoundaryCallback.test(signalData)) {
                            Create.RAILWAYS.sync.edgeDataChanged(graph, currentNode, nextNode, edge, oppositeEdge);
                        }
                        continue;
                    }

                    // other/own boundary found
                    TrackCircuitBoundary nextBoundary = signalData.next(AllEdgeTypes.TRACK_CIRCUIT_BOUNDARY, 0);
                    if (nextBoundary == null)
                        continue;
                    if (circuitCallback.test(Pair.of(currentNode, nextBoundary))) {
                        currentEdge.getEdgeData()
                                .refreshIntersectingSignalGroups(graph);
                        Create.RAILWAYS.sync.edgeDataChanged(graph, currentNode, nextNode, edge, oppositeEdge);
                    }
                    continue EdgeWalk;
                }

                frontier.add(Couple.create(nextNode, currentNode));
            }
        }
    }
}
