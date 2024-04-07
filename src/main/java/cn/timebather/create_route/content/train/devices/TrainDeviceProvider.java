package cn.timebather.create_route.content.train.devices;

public interface TrainDeviceProvider {
    TrainDeviceType<? extends TrainDevice> getDevice();
}
