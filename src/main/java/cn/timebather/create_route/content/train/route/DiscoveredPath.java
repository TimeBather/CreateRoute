package cn.timebather.create_route.content.train.route;

import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.foundation.utility.Couple;

import java.util.List;

public class DiscoveredPath {
    public double distance;
    public double cost;
    public List<Couple<TrackNode>> path;
    public SignalBoundary destination;

    public DiscoveredPath(
            double distance,
            double cost,
            List<Couple<TrackNode>> path,
            SignalBoundary destination
    ){
        this.distance = distance;
        this.cost = cost;
        this.path = path;
        this.destination = destination;
    }
}
