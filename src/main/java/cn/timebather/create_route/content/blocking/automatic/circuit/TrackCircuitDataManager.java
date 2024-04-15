package cn.timebather.create_route.content.blocking.automatic.circuit;

import java.util.HashMap;
import java.util.UUID;

public class TrackCircuitDataManager {
    HashMap<UUID,TrackCircuit> circuits = new HashMap<>();

    public void removeCircuit(UUID id) {
        circuits.remove(id);
    }

    public void addCircuit(TrackCircuit circuit){
        circuits.put(circuit.id,circuit);
    }
}
