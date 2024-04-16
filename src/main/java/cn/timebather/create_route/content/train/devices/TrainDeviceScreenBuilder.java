package cn.timebather.create_route.content.train.devices;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.client.gui.screens.Screen;

public interface TrainDeviceScreenBuilder<T extends TrainDevice> {
    public Screen create(T device, Carriage carriage);
}
