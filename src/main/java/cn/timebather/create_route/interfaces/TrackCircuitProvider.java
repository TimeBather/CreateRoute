package cn.timebather.create_route.interfaces;

import java.util.UUID;

public interface TrackCircuitProvider {
    public boolean createRoute$hasTrackCircuitBoundary();

    public void createRoute$setTrackCircuitId(UUID id);

    public UUID createRoute$getTrackCircuitId();
}
