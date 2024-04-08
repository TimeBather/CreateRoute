package cn.timebather.create_route.content.train.packets;

import java.util.HashMap;
import java.util.function.Consumer;

public class DeviceMessageDispatcher<T> {
    HashMap<TrainDevicePeerPacket.PeerIdentifier,Consumer<T>> hashMap = new HashMap<>();

    public T value;

    public void order(TrainDevicePeerPacket.PeerIdentifier identifier, Consumer<T> consumer){
        this.hashMap.put(identifier,consumer);
        consumer.accept(value);
    }

    public void disorder(TrainDevicePeerPacket.PeerIdentifier identifier){
        this.hashMap.remove(identifier);
    }

    public void setValue(T newValue){
        this.value = newValue;
        this.hashMap.values().forEach((c)->c.accept(newValue));
    }
}
