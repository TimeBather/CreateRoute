package cn.timebather.create_route.content.train.route;

import cn.timebather.create_route.mixins.accessor.CarriageBogeyAccessor;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.*;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import java.util.Map.Entry;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

// The code is inspired by Create
public class Navigation {
    Train train;
    SignalBoundary destination;

    public boolean destinationBehindTrain;

    @Nullable
    public DiscoveredPath findPathTo(SignalBoundary destinations,double maxCost){
        TrackGraph graph = this.train.graph;
        if(graph == null){
            return null;
        }
        Couple<DiscoveredPath> results = Couple.create(null,null);

        for (boolean forward : Iterate.trueAndFalse) {
            if (this.destination != null && destinationBehindTrain == forward)
                continue;
            TravellingPoint initialPoint = forward ? train.carriages.get(0)
                    .getLeadingPoint()
                    : train.carriages.get(train.carriages.size() - 1)
                    .getTrailingPoint();
            TrackEdge initialEdge = forward ? initialPoint.edge
                    : graph.getConnectionsFrom(initialPoint.node2)
                    .get(initialPoint.node1);
            search(Double.MAX_VALUE,maxCost,forward,destinations,(distance, cost, reachedVia, currentEntry, signal) -> {
                TrackEdge edge = currentEntry.getSecond();
                TrackNode node1 = currentEntry.getFirst()
                        .getFirst();
                TrackNode node2 = currentEntry.getFirst()
                        .getSecond();

                List<Couple<TrackNode>> currentPath = new ArrayList<>();
                Pair<Boolean, Couple<TrackNode>> backTrack = reachedVia.get(edge);
                Couple<TrackNode> toReach = Couple.create(node1, node2);
                TrackEdge edgeReached = edge;
                while (backTrack != null) {
                    if (edgeReached == initialEdge)
                        break;
                    if (backTrack.getFirst())
                        currentPath.add(0, toReach);
                    toReach = backTrack.getSecond();
                    edgeReached = graph.getConnection(toReach);
                    backTrack = reachedVia.get(edgeReached);
                }

                double position = edge.getLength() - destination.getLocationOn(edge);
                double distanceToDestination = distance - position;
                results.set(forward, new DiscoveredPath((forward ? 1 : -1) * distanceToDestination, cost, currentPath, destination));
                return true;
            });
        }
        DiscoveredPath front = results.getFirst();
        DiscoveredPath back = results.getSecond();

        boolean frontEmpty = front == null;
        boolean backEmpty = back == null;
        boolean canDriveForward = train.hasForwardConductor() || train.runtime.paused;
        boolean canDriveBackward = train.doubleEnded && train.hasBackwardConductor() || train.runtime.paused;

        if (backEmpty || !canDriveBackward)
            return canDriveForward ? front : null;
        if (frontEmpty || !canDriveForward)
            return canDriveBackward ? back : null;

        // boolean frontBetter = maxCost == -1 ? -back.distance > front.distance : back.cost > front.cost;
        // return frontBetter ? front : back;
        return front; // Front is always better than backward because no-direction change
    }


    public void search(double maxDistance, double maxCost, boolean forward, SignalBoundary destination, SignalTest signalTest) {
        TrackGraph graph = train.graph;
        if (graph == null)
            return;

        // Cache the list of track types that the train can travel on
        Set<TrackMaterial.TrackType> validTypes = new HashSet<>();
        for (int i = 0; i < train.carriages.size(); i++) {
            Carriage carriage = train.carriages.get(i);
            if (i == 0) {
                validTypes.addAll(((CarriageBogeyAccessor)carriage.leadingBogey()).getType().getValidPathfindingTypes(carriage.leadingBogey().getStyle()));
            } else {
                validTypes.retainAll(((CarriageBogeyAccessor)carriage.leadingBogey()).getType().getValidPathfindingTypes(carriage.leadingBogey().getStyle()));
            }
            if (carriage.isOnTwoBogeys())
                validTypes.retainAll(((CarriageBogeyAccessor)carriage.leadingBogey()).getType().getValidPathfindingTypes(carriage.trailingBogey().getStyle()));
        }
        if (validTypes.isEmpty()) // if there are no valid track types, a route can't be found
            return;

        Map<TrackEdge, Integer> penalties = new IdentityHashMap<>();
        boolean costRelevant = maxCost >= 0;
        if (costRelevant) {
            for (Train otherTrain : Create.RAILWAYS.trains.values()) {
                if (otherTrain.graph != graph)
                    continue;
                if (otherTrain == train)
                    continue;
                int navigationPenalty = otherTrain.getNavigationPenalty();
                otherTrain.getEndpointEdges()
                        .forEach(nodes -> {
                            if (nodes.either(Objects::isNull))
                                return;
                            for (boolean flip : Iterate.trueAndFalse) {
                                TrackEdge e = graph.getConnection(flip ? nodes.swap() : nodes);
                                if (e == null)
                                    continue;
                                int existing = penalties.getOrDefault(e, 0);
                                penalties.put(e, existing + navigationPenalty / 2);
                            }
                        });
            }
        }

        TravellingPoint startingPoint = forward ? train.carriages.get(0)
                .getLeadingPoint()
                : train.carriages.get(train.carriages.size() - 1)
                .getTrailingPoint();

        Set<TrackEdge> visited = new HashSet<>();
        Map<TrackEdge, Pair<Boolean, Couple<TrackNode>>> reachedVia = new IdentityHashMap<>();
        PriorityQueue<FrontierEntry> frontier = new PriorityQueue<>();

        TrackNode initialNode1 = forward ? startingPoint.node1 : startingPoint.node2;
        TrackNode initialNode2 = forward ? startingPoint.node2 : startingPoint.node1;
        TrackEdge initialEdge = graph.getConnectionsFrom(initialNode1)
                .get(initialNode2);
        if (initialEdge == null)
            return;

        double distanceToNode2 = forward ? initialEdge.getLength() - startingPoint.position : startingPoint.position;


        // Apply penalties to initial edge
        int initialPenalty = 0;
        if (costRelevant)
            initialPenalty += penalties.getOrDefault(initialEdge, 0);

        EdgeData initialSignalData = initialEdge.getEdgeData();
        if (initialSignalData.hasPoints()) {
            for (TrackEdgePoint point : initialSignalData.getPoints()) {
                if (point.getLocationOn(initialEdge) < initialEdge.getLength() - distanceToNode2)
                    continue;
                if (costRelevant && distanceToNode2 + initialPenalty > maxCost)
                    return;
                if (!point.canNavigateVia(initialNode2))
                    return;
                if (point instanceof SignalBoundary boundary) {
                    if(boundary.canNavigateVia(initialNode2) && signalTest.test(distanceToNode2, distanceToNode2 + initialPenalty, reachedVia,
                            Pair.of(Couple.create(initialNode1, initialNode2), initialEdge), boundary))
                        return;
                }
            }
        }

        if (costRelevant && distanceToNode2 + initialPenalty > maxCost)
            return;

        frontier.add(new FrontierEntry(distanceToNode2, initialPenalty, initialNode1, initialNode2, initialEdge));

        while (!frontier.isEmpty()) {
            FrontierEntry entry = frontier.poll();
            if (!visited.add(entry.edge))
                continue;

            double distance = entry.distance;
            int penalty = entry.penalty;

            if (distance > maxDistance)
                continue;

            TrackEdge edge = entry.edge;
            TrackNode node1 = entry.node1;
            TrackNode node2 = entry.node2;

            if (entry.hasDestination) {
                EdgeData signalData = edge.getEdgeData();
                if (signalData.hasPoints()) {
                    for (TrackEdgePoint point : signalData.getPoints()) {
                        if (point instanceof SignalBoundary boundary) {
                            if (boundary.canNavigateVia(node2) && signalTest.test(distance, penalty, reachedVia,
                                    Pair.of(Couple.create(node1, node2), edge), boundary))
                                return;
                        }
                    }
                }
            }

            List<Entry<TrackNode, TrackEdge>> validTargets = new ArrayList<>();
            Map<TrackNode, TrackEdge> connectionsFrom = graph.getConnectionsFrom(node2);
            for (Entry<TrackNode, TrackEdge> connection : connectionsFrom.entrySet()) {
                TrackNode newNode = connection.getKey();
                if (newNode == node1)
                    continue;
                if (edge.canTravelTo(connection.getValue()))
                    validTargets.add(connection);
            }

            if (validTargets.isEmpty())
                continue;

            Search: for (Entry<TrackNode, TrackEdge> target : validTargets) {
                if (!validTypes.contains(target.getValue().getTrackMaterial().trackType))
                    continue;
                TrackNode newNode = target.getKey();
                TrackEdge newEdge = target.getValue();
                int newPenalty = penalty;
                double edgeLength = newEdge.getLength();
                double newDistance = distance + edgeLength;

                if (costRelevant)
                    newPenalty += penalties.getOrDefault(newEdge, 0);

                // Apply penalty to next connected edge
                boolean hasDestination = false;
                EdgeData signalData = newEdge.getEdgeData();
                if (signalData.hasPoints()) {
                    for (TrackEdgePoint point : signalData.getPoints()) {
                        if (node2 == initialNode1 && point.getLocationOn(newEdge) < edgeLength - distanceToNode2)
                            continue;
                        if (costRelevant && newDistance + newPenalty > maxCost)
                            continue Search;
                        if (!point.canNavigateVia(newNode))
                            continue Search;
                        if (point instanceof SignalBoundary boundary) {
                            if (boundary.canNavigateVia(node2) && signalTest.test(newDistance, newDistance + newPenalty, reachedVia,
                                    Pair.of(Couple.create(node2, newNode), newEdge), boundary)){
                                hasDestination = true;
                                continue;
                            }
                        }
                    }
                }

                if (costRelevant && newDistance + newPenalty > maxCost)
                    continue;

                double remainingDist = 0;
                // Calculate remaining distance estimator for next connected edge
                if (destination != null) {
                    remainingDist = Double.MAX_VALUE;
                    Vec3 newNodePosition = newNode.getLocation().getLocation();
                    TrackNodeLocation destinationNode = destination.edgeLocation.getFirst();
                    double dMin = Math.abs(newNodePosition.x - destinationNode.getLocation().x);
                    double dMid = Math.abs(newNodePosition.y - destinationNode.getLocation().y);
                    double dMax = Math.abs(newNodePosition.z - destinationNode.getLocation().z);
                    double temp;
                    if (dMin > dMid) {
                        temp = dMid;
                        dMid = dMin;
                        dMin = temp;
                    }
                    if (dMin > dMax) {
                        temp = dMax;
                        dMax = dMin;
                        dMin = temp;
                    }
                    if (dMid > dMax) {
                        temp = dMax;
                        dMax = dMid;
                        dMid = temp;
                    }
                    // Octile distance from newNode to station node
                    double currentRemaining = 0.317837245195782 * dMin + 0.414213562373095 * dMid + dMax + destination.position;
                    if (node2.getLocation().equals(destinationNode))
                        currentRemaining -= newEdge.getLength() * 2; // Correct the distance estimator for station edge
                    remainingDist = Math.min(remainingDist, currentRemaining);

                }

                reachedVia.putIfAbsent(newEdge, Pair.of(validTargets.size() > 1, Couple.create(node1, node2)));
                frontier.add(new FrontierEntry(newDistance, newPenalty, remainingDist, hasDestination, node2, newNode, newEdge));
            }
        }
    }

    private static class FrontierEntry implements Comparable<FrontierEntry> {

        double distance;
        int penalty;
        double remaining;
        boolean hasDestination;
        TrackNode node1;
        TrackNode node2;
        TrackEdge edge;

        public FrontierEntry(double distance, int penalty, TrackNode node1, TrackNode node2, TrackEdge edge) {
            this.distance = distance;
            this.penalty = penalty;
            this.remaining = 0;
            this.hasDestination = false;
            this.node1 = node1;
            this.node2 = node2;
            this.edge = edge;
        }
        public FrontierEntry(double distance, int penalty, double remaining, boolean hasDestination, TrackNode node1, TrackNode node2, TrackEdge edge) {
            this.distance = distance;
            this.penalty = penalty;
            this.remaining = remaining;
            this.hasDestination = hasDestination;
            this.node1 = node1;
            this.node2 = node2;
            this.edge = edge;
        }

        @Override
        public int compareTo(FrontierEntry o) {
            return Double.compare(distance + penalty + remaining, o.distance + o.penalty + o.remaining);
        }
    }

    @FunctionalInterface
    public static interface SignalTest {
        boolean test(double distance, double cost, Map<TrackEdge, Pair<Boolean, Couple<TrackNode>>> reachedVia,
                     Pair<Couple<TrackNode>, TrackEdge> current, SignalBoundary station);
    }
}
