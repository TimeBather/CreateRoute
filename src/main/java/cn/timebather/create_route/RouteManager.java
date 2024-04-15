package cn.timebather.create_route;

import cn.timebather.create_route.content.blocking.automatic.circuit.TrackCircuitDataManager;
import cn.timebather.create_route.content.train.devices.TrainDevice;

import java.util.HashMap;
import java.util.UUID;

public class RouteManager {
    public HashMap<UUID,TrainDevice> allTrainDevices = new HashMap<>();

    public TrackCircuitDataManager circuitDataManager = new TrackCircuitDataManager();
}
