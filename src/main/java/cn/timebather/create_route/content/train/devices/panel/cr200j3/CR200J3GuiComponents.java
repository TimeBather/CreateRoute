package cn.timebather.create_route.content.train.devices.panel.cr200j3;

import cn.timebather.create_route.foundation.gui.GuiTexture;
import cn.timebather.create_route.foundation.gui.GuiTextureHelper;

public class CR200J3GuiComponents extends GuiTextureHelper {
    protected static GuiTexture CONTROLLER = register("panel/cr200j3/controller");

    public static GuiTexture PANEL_MAIN = CONTROLLER.clip(0,0,256,48);
    public static GuiTexture BUTTON_MAIN = CONTROLLER.clip(0,48,8,8);
    public static GuiTexture BUTTON_CONTENT = CONTROLLER.clip(8,48,8,8);
}
