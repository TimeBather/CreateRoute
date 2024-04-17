package cn.timebather.create_route.content.train.devices.panel.cr200j3.item;

import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.*;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceProvider;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import net.minecraft.world.item.Item;

public class CR200J3PanelItem extends Item implements TrainDeviceProvider {
    public CR200J3PanelItem(Properties properties) {
        super(properties);
    }

    @Override
    public TrainDeviceType<? extends TrainDevice> getDevice() {
        return AllTrainDevices.PANEL_CR200J3.get();
    }
}
