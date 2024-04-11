package cn.timebather.create_route.foundation.gui;

import cn.timebather.create_route.CreateRoute;
import net.minecraft.resources.ResourceLocation;

public class GuiTextureHelper {
    protected static GuiTexture register(String path, int textureX, int textureY, int textureWidth, int textureHeight){
        return GuiTexture.of(CreateRoute.asResource("textures/gui/" + path + ".png"),textureX,textureY,textureWidth,textureHeight);
    }

    protected static GuiTexture register(String path, int textureWidth, int textureHeight){
        return GuiTexture.of(CreateRoute.asResource("textures/gui/" + path + ".png"),textureWidth,textureHeight);
    }

    protected static GuiTexture register(String path){
        return GuiTexture.of(CreateRoute.asResource("textures/gui/" + path + ".png"));
    }
}
