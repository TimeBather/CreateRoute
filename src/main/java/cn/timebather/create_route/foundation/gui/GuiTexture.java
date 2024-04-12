package cn.timebather.create_route.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class GuiTexture {
    ResourceLocation location;

    int textureX,textureY,textureWidth,textureHeight;

    double scale = 1;

    protected GuiTexture(ResourceLocation location, int textureX, int textureY, int textureWidth, int textureHeight){
        this.location = location;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    protected GuiTexture(ResourceLocation location, int textureWidth, int textureHeight){
        this(location,0,0,textureWidth,textureHeight);
    }
    protected GuiTexture(ResourceLocation location){
        this(location,0,0,256,256);
    }

    public static GuiTexture of(ResourceLocation location, int textureX, int textureY, int textureWidth, int textureHeight){
        return new GuiTexture(location,textureX,textureY,textureWidth,textureHeight);
    }

    public static GuiTexture of(ResourceLocation location, int textureWidth, int textureHeight){
        return new GuiTexture(location,textureWidth,textureHeight);
    }

    public static GuiTexture of(ResourceLocation location){
        return new GuiTexture(location);
    }

    public GuiTexture clipDelta(int deltaX,int deltaY,int deltaWidth,int deltaHeight){
        return GuiTexture.of(
                location,
                textureX + deltaX,
                textureY + deltaY,
                textureWidth + deltaWidth - deltaX,
                textureHeight + deltaHeight - deltaY
        );
    }

    public GuiTexture clip(int x, int y, int width, int height){
        return GuiTexture.of(
                location,
                x + textureX,
                y + textureY,
                width,
                height
        );
    }

    public GuiTexture withScale(double scale){
        GuiTexture texture = new GuiTexture(location,textureX,textureY,textureWidth,textureHeight);
        texture.setScale(scale);
        return texture;
    }

    private void setScale(double scale) {
        this.scale = scale;
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height){
        graphics.blit(location,x,y,width,height,(float)textureX,(float)textureY,textureWidth,textureHeight,256,256);

    }

    public void render(GuiGraphics graphics, int x, int y){
        render(graphics,x,y,(int)(textureWidth * scale),(int)(textureHeight * scale));
    }

    public void render(GuiGraphics graphics, Color color, int x0, int y0, int width, int height){
        RenderSystem.setShaderTexture(0,location);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder vertexConsumer = tesselator.getBuilder();


        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        vertexConsumer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);


        Matrix4f pose = graphics.pose().last().pose();
        float x1 = x0 + width;
        float y1 = y0 + height;
        float u0 = textureX / 256f;
        float v0 = textureY / 256f;
        float u1 = u0 + textureWidth / 256f;
        float v1 = v0 + textureHeight / 256f;
        float z = 0;

        vertexConsumer.vertex(pose,(float)x0,y1,z).color(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()).uv(u0,v1).endVertex();
        vertexConsumer.vertex(pose,x1,y1,z).color(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()).uv(u1,v1).endVertex();
        vertexConsumer.vertex(pose,x1,(float)y0,z).color(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()).uv(u1,v0).endVertex();
        vertexConsumer.vertex(pose,(float)x0,(float)y0,z).color(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()).uv(u0,v0).endVertex();

        tesselator.end();

        RenderSystem.disableBlend();
    }

    public void render(GuiGraphics graphics,Color color,int x,int y){
        render(graphics,color,x,y,(int)(textureWidth * scale),(int)(textureHeight * scale));
    }
}
