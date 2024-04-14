package cn.timebather.create_route.content.train.devices.panel.cr200j3.screen;

import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3GuiComponents;
import cn.timebather.create_route.foundation.gui.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class CR200J3GuiButton extends AbstractWidget {
    private final Color color;

    public CR200J3GuiButton(int x, int y, Color color) {
        super(x, y, 8, 8, Component.literal(""));
        this.color = color;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        CR200J3GuiComponents.BUTTON_CONTENT.render(guiGraphics,color,getX(),getY());
        CR200J3GuiComponents.BUTTON_MAIN.render(guiGraphics,getX(),getY());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void onClick(double p_93634_, double p_93635_) {

    }

    @Override
    public void onRelease(double p_93669_, double p_93670_) {
        super.onRelease(p_93669_, p_93670_);
    }
}
