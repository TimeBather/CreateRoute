package cn.timebather.create_route.content.train.devices.controller.screens;


import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import cn.timebather.create_route.content.train.devices.controller.api.TrainControllerClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TrainControllerControlScreen extends Screen {
    private final TrainControllerClient client;

    protected TrainControllerControlScreen(TrainControllerDevice device) {
        super(Component.literal(""));
        this.client = device.getClient();
    }

    @Override
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        super.render(p_281549_, p_281550_, p_282878_, p_282465_);
    }
}
