package cn.timebather.create_route.content.train.devices.atp.lkj2000;

import cn.timebather.create_route.foundation.gui.GuiTexture;
import cn.timebather.create_route.foundation.gui.GuiTextureHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class LKJ2000GuiComponents extends GuiTextureHelper {

    protected static final GuiTexture ENTRY_TEXTURE = register("lkj2000/entry");

    public static final GuiTexture LKJ_ENTRY
            = ENTRY_TEXTURE.clip(0,0,256,192);

    public static final GuiTexture LKJ_SIGNAL_AREA
            = ENTRY_TEXTURE.clip(0,192,66,42)
            .withScale(0.5);

    public static final GuiTexture LKJ_SIGNAL
            = ENTRY_TEXTURE.clip(66,192,34,34)
            .withScale(0.5);
    public static final GuiTexture LKJ_SIGNAL_LIGHT
            = ENTRY_TEXTURE.clip(100,192,14,13)
            .withScale(0.5);

    public static final GuiTexture LKJ_STATUS_LIGHT_SMALL_BORDER =
            ENTRY_TEXTURE.clip(0,234,17,9);

    public static final GuiTexture LKJ_STATUS_LIGHT_SMALL_CONTENT =
            ENTRY_TEXTURE.clip(0,243,17,9);

    public static final GuiTexture LKJ_GRAPH_SIGNAL_DOUBLE =
            ENTRY_TEXTURE.clip(115,192,13,63);

    public static final GuiTexture LKJ_GRAPH_SIGNAL_SINGLE=
            LKJ_GRAPH_SIGNAL_DOUBLE.clipDelta(0,13,0,0);

    protected static final GuiTexture LKJ_PANEL = register("lkj2000/panel");

    protected static final GuiTexture LKJ_FONT = register("lkj2000/fonts");

    public static final GuiTexture LKJ_FONT_SPEED = LKJ_FONT.clip(0,2,15,40);

    public static final GuiTexture LKJ_FONT_SPEED_LIMIT = LKJ_FONT.clip(16,2,15,40);
    public static final GuiTexture LKJ_FONT_DISTANCE = LKJ_FONT.clip(32,2,15,40);
    public static final GuiTexture LKJ_FONT_SIGNAL = LKJ_FONT.clip(50,2,31,16);
    public static final GuiTexture LKJ_FONT_RAIL_DISTANCE = LKJ_FONT.clip(48,27,31,16);
    public static final GuiTexture LKJ_FONT_RBC = LKJ_FONT.clip(80,3,47,16);
    // public static final GuiTexture LKJ_FONT_RBC = LKJ_FONT.clip(80,3,47,16);
    public static final GuiTexture LKJ_FONT_SLOPE = LKJ_FONT.clip(80,26,47,16);
    public static final GuiTexture LKJ_FONT_CURVE = LKJ_FONT.clip(128,2,32,16);
    public static final GuiTexture LKJ_FONT_DOWNGRADE = LKJ_FONT.clip(128,26,32,16);
    public static final GuiTexture LKJ_FONT_COMMON_BREAK = LKJ_FONT.clip(160,2,32,16);
    public static final GuiTexture LKJ_FONT_EMERGENCY_BREAK = LKJ_FONT.clip(160,26,32,16);
    public static final GuiTexture LKJ_STATUS_LIGHT_WRAPPER = LKJ_ENTRY.clip(0,234,17,10);
    public static final GuiTexture LKJ_STATUS_LIGHT = LKJ_ENTRY.clip(0,244,17,10);


}
