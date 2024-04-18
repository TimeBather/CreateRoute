package cn.timebather.create_route.content.train.devices.controller.screens;


import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceScreenBuilder;
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
        if(activeScreen != null)
            activeScreen.render(graphics,mouseX,mouseY,pTicks);
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
            @SuppressWarnings("unchecked")
            TrainDeviceScreenBuilder<TrainDevice> screenBuilder =
                    (TrainDeviceScreenBuilder<TrainDevice>) subDevice.getType().getScreenBuilder().get();
            if(screenBuilder == null)
                return;
            ResourceLocation location = AllTrainDevices.REGISTRY.get().getKey(subDevice.getType());
            if (location == null)
                continue;
            Screen subScreen = screenBuilder.create(subDevice,this.carriage);
            subScreen.init(minecraft,width,height);
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
        if(this.activeScreen == null)
            return false;
        this.activeScreen.mouseDragged(p_94699_,p_94700_,p_94701_,p_94702_,p_94703_);
        return true;
    }

    @Override
    public boolean mouseClicked(double x, double y, int key) {
        if(super.mouseClicked(x,y,key))
            return true;
        if(this.activeScreen == null){
            return false;
        }
        return this.activeScreen.mouseClicked(x,y,key);
    }

    @Override
    public boolean mouseReleased(double x, double y, int key) {
        if(super.mouseClicked(x,y,key))
            return true;
        if(this.activeScreen == null){
            return false;
        }
        return this.activeScreen.mouseReleased(x,y,key);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double keyCode) {
        return super.mouseScrolled(x, y, keyCode);
    }
}
