package cn.timebather.create_route.content.train.devices.controller;

import cn.timebather.create_route.content.train.devices.controller.screens.TrainControllerControlScreen;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.client.Minecraft;

public class TrainControllerClientSideBridge {
    public static void openScreen(TrainControllerDevice device, Carriage carriage){
        Minecraft.getInstance().setScreen(new TrainControllerControlScreen(device,carriage));
    }
}
