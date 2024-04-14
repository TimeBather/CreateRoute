package cn.timebather.create_route.content.train.devices.panel.cr200j3.screen;

import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3GuiComponents;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3PanelDevice;
import cn.timebather.create_route.foundation.gui.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CR200J3PanelScreen extends Screen {
    private final CR200J3PanelDevice device;
    private int x;
    private int y;

    public CR200J3PanelScreen(CR200J3PanelDevice device) {
        super(Component.literal(""));
        this.device = device;
    }

    @Override
    protected void init() {
        this.x = (int) ((width - 256) * 0.5f);
        this.y = height - 68;
        this.addRenderableWidget(new CR200J3GuiButton(x+6,y+9, Color.RED));
        this.addRenderableWidget(new CR200J3GuiButton(x+18,y+9, Color.RED));
        this.addRenderableWidget(new CR200J3GuiButton(x+30,y+9, Color.RED));
        this.addRenderableWidget(new CR200J3GuiButton(x+6,y+26, Color.RED));
        this.addRenderableWidget(new CR200J3GuiButton(x+18,y+26, Color.RED));
        this.addRenderableWidget(new CR200J3GuiButton(x+30,y+26, Color.RED));
        CR200J3GuiSlider airBreakSlider = new CR200J3GuiSlider(x+42,y+5,10,6,32,32);
        this.addRenderableWidget(airBreakSlider);
        airBreakSlider.onUpdate((val)->{
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Air break set to:"+val.toString()));
            this.device.setAirBreak(32 - val);
        });
        this.addRenderableWidget(new CR200J3GuiSlider(x+59,y+5,10,6,32,32));
        CR200J3GuiSlider powerSlider = new CR200J3GuiSlider(x+204,y+5,10,6,32,24);
        powerSlider.onUpdate((val)->{
            int power = 12 - val / 2;
            String powerString = ( power >= 0 ? "P" : "N" ) + String.valueOf(Math.abs(power));
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Power set to:"+powerString));
            this.device.setPower(power);
        });


        this.addRenderableWidget(powerSlider);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pTicks) {
        CR200J3GuiComponents.PANEL_MAIN.render(graphics,x,y);
        super.render(graphics,mouseX,mouseY,pTicks);
    }
}
