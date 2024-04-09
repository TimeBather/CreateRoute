package cn.timebather.create_route.content.train.devices.controller.screens;


import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.ScreenDevice;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import cn.timebather.create_route.content.train.devices.controller.api.TrainControllerClient;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;

public class TrainControllerControlScreen extends Screen {
    private final TrainControllerClient client;
    private final Carriage carriage;
    private final TrainControllerDevice device;

    private TrainDevice current;
    private Button previousButton;

    private Screen activeScreen;

    private ArrayList<Button> switchButtons = new ArrayList<>();

    private ArrayList<Screen> subScreens = new ArrayList<>();

    public TrainControllerControlScreen(TrainControllerDevice device, Carriage carriage) {
        super(Component.literal(""));
        this.device = device;
        this.client = device.getClient();
        this.carriage = carriage;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pTicks) {
        super.render(graphics, mouseX, mouseY, pTicks);
        for (Screen subScreen : subScreens) {
            subScreen.render(graphics,mouseX,mouseY,pTicks);
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (Screen subScreen : subScreens) {
            subScreen.tick();
        }
    }

    public void init(){
        subScreens.clear();
        switchButtons.clear();
        Collection<TrainDevice> subDevices = this.device.getSubDevices();
        int i = 0;
        for (TrainDevice subDevice : subDevices) {
            if(!(subDevice instanceof ScreenDevice screenProvider))
                continue;
            ResourceLocation location = AllTrainDevices.REGISTRY.get().getKey(subDevice.getType());
            if (location == null)
                continue;
            Screen subScreen = screenProvider.createScreen();
            subScreen.init(minecraft,height,width);
            subScreens.add(subScreen);
            Button button = Button.builder(Component.translatable(location.toLanguageKey("device")), (event) -> {
                current = subDevice;
                if (previousButton != null)
                    previousButton.active = true;
                event.active = false;
                previousButton = event;
                activeScreen = subScreen;
            }).pos(i, height - 20).size(80, 20).build();
            i += 80;
            switchButtons.add(button);
            this.addRenderableWidget(button);
        }
        previousButton = switchButtons.get(0);
        activeScreen = subScreens.get(0);
        previousButton.active = false;
        current = subDevices.iterator().next();
    }
}
