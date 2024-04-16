package cn.timebather.create_route.content.train.devices.atp.lkj2000.screen;

import cn.timebather.create_route.content.train.devices.atp.lkj2000.LKJ2000GuiComponents;
import cn.timebather.create_route.foundation.gui.Color;
import net.minecraft.client.gui.GuiGraphics;

public class LKJ2000BackgroundRenderer {
    public static void render(GuiGraphics graphics){
        LKJ2000GuiComponents.LKJ_ENTRY.render(graphics,0,0);
        LKJ2000GuiComponents.LKJ_SIGNAL_AREA.render(graphics,9,7);
        LKJ2000GuiComponents.LKJ_SIGNAL.render(graphics, Color.WHITE,9+2,7+2);
        LKJ2000GuiComponents.LKJ_SIGNAL_LIGHT.render(graphics,9+2+1,7+2+1);
        LKJ2000GuiComponents.LKJ_FONT_SPEED.withScale(0.35).render(graphics,Color.BLUE ,45,10);
        LKJ2000GuiComponents.LKJ_FONT_SPEED_LIMIT.withScale(0.35).render(graphics,Color.BLUE,81,10);
        LKJ2000GuiComponents.LKJ_FONT_DISTANCE.withScale(0.35).render(graphics,Color.BLUE,116,10);
        LKJ2000GuiComponents.LKJ_FONT_SIGNAL.render(graphics,Color.BLUE,170,8,10,6);
        LKJ2000GuiComponents.LKJ_FONT_RAIL_DISTANCE.render(graphics,Color.BLUE,170,17,9,6);

        LKJ2000GuiComponents.LKJ_FONT_SLOPE.render(graphics,Color.BLACK,8,145,15,8);
        LKJ2000GuiComponents.LKJ_FONT_CURVE.render(graphics,Color.BLACK,8,156,15,8);
        LKJ2000GuiComponents.LKJ_FONT_RBC.render(graphics,Color.BLACK,8,168,15,8);
    }
}
