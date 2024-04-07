package cn.timebather.create_route.content.train.devices.atp.lkj2000.screen;

import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Client;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

public class LKJ2000Screen extends Screen {

    private final LKJ2000Client client;

    LKJ2000Screen(LKJ2000Client client) {
        super(Component.literal(""));
        this.client = client;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
        graphics.flush();
        Matrix4f pose = graphics.pose().last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.lines());
        buffer.vertex(pose,0,0,100).color(1f,1f,1f,1f).endVertex();
        buffer.vertex(pose,mouseX,mouseY,100).color(1f,1f,1f,1f).endVertex();
        graphics.flush();

    }
}