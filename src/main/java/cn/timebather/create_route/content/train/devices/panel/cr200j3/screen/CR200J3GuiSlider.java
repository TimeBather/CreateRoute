package cn.timebather.create_route.content.train.devices.panel.cr200j3.screen;

import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3GuiComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class CR200J3GuiSlider extends AbstractWidget {

    private final int maxY;
    private final int ySize;

    public CR200J3GuiSlider(int x, int y, int w, int h, int maxY,int current) {
        super(x,y, w,maxY , Component.literal(""));
        this.maxY = maxY;
        this.ySize = h;
        this.deltaY = current;
    }

    float deltaY;

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.fill(getX(),getY() + (int)deltaY,getX()+getWidth(),getY()+ySize + (int)deltaY,0xFFFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected void onDrag(double p_93636_, double p_93637_, double vX, double vY) {
        double oldDeltaY = deltaY;
        deltaY += (float) vY;
        if(deltaY > maxY){
            deltaY = maxY;
        }
        if(deltaY < 0){
            deltaY = 0;
        }
        if((int)deltaY!=(int)oldDeltaY){
            updateHook.accept((int)deltaY);
        }
    }

    Consumer<Integer> updateHook = (v)->{};

    public void onUpdate(Consumer<Integer> updateHook) {
        this.updateHook = updateHook;
    }
}
