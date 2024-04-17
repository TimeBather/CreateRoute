package cn.timebather.create_route.content.train.devices.atp.lkj2000.items;

import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceProvider;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import net.minecraft.world.item.Item;

public class LKJ2000ControllerItem extends Item implements TrainDeviceProvider {
    public LKJ2000ControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public TrainDeviceType<? extends TrainDevice> getDevice() {
        return AllTrainDevices.LKJ_2000.get();
    }
}
