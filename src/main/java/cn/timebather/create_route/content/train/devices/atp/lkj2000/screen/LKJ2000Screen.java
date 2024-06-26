package cn.timebather.create_route.content.train.devices.atp.lkj2000.screen;

import cn.timebather.create_route.content.train.devices.atp.lkj2000.LKJ2000Device;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.LKJ2000GuiComponents;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Client;
import cn.timebather.create_route.foundation.gui.Color;
import cn.timebather.create_route.foundation.gui.GuiLiner;
import cn.timebather.create_route.foundation.gui.GuiTexture;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class LKJ2000Screen extends Screen {

    private final LKJ2000Client client;

    private static final ItemStack clockStack = new ItemStack(Items.CLOCK,1);
    private final LKJ2000Device device;

    String speedStr = "0";

    String distanceStr = "-----";


    ArrayList<BiConsumer<VertexConsumer, Matrix4f>> layout_lines = new ArrayList<>();
    ArrayList<BiConsumer<VertexConsumer, Matrix4f>> element_lines = new ArrayList<>();

    private int warningTicks ;


    public LKJ2000Screen(LKJ2000Device device, Carriage carriage) {
        super(Component.literal(""));
        this.client = device.getClient();
        this.device = device;
    }

    @Override
    public void tick() {
        this.warningTicks++;
        if(warningTicks > 40){
            warningTicks = 0;
        }
        this.element_lines.clear();
        double lastY = 0;
        boolean hasLastY = false;
        ArrayList<Pair<Double, Double>> speedLines = this.device.getLastSpeedCurve();
        double x = 68 - this.device.getRecordedDistance() / 23.8905d;
        for (Pair<Double, Double> y : speedLines) {
            if(!hasLastY){
                lastY = y.getFirst();
                hasLastY = true;
                continue;
            }
            double screenDeltaDistance = y.getSecond() / 23.8095d;
            element_lines.add(GuiLiner.line((float)x,(float)(140-lastY * 0.7),(float)(x+screenDeltaDistance),(float)(140-y.getFirst() * 0.7),0.2f,Color.GREEN));
            x += screenDeltaDistance;
            lastY = y.getFirst();
        }
        Carriage carriage = this.device.getCarriage();
        if(carriage != null && carriage.train !=null){
            int speed = (int)Math.floor(carriage.train.speed * 3.6 * 20);
            this.speedStr = String.valueOf(speed);
        }
    }

    @Override
    protected void init() {
        layout_lines.clear();
        layout_lines.add(GuiLiner.line( 68,34,68,141,0.2f, Color.YELLOW));
        layout_lines.add(GuiLiner.line(26,154,236,154,0.5f, Color.hex(0xFF3C3B67)));
        layout_lines.add(GuiLiner.line(26,166,236,166,0.5f, Color.hex(0xFF3C3B67)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(width,height - 20,0);
        poseStack.scale(0.75f,0.75f,1f);
        poseStack.translate( - 256,- 192 - 20,0);

        LKJ2000BackgroundRenderer.render(graphics);

        renderStatus(graphics,Color.YELLOW,Color.BLACK,LKJ2000GuiComponents.LKJ_FONT_DOWNGRADE,33);
        renderStatus(graphics,Color.RED,Color.YELLOW,LKJ2000GuiComponents.LKJ_FONT_EMERGENCY_BREAK,43);
        renderStatus(graphics,Color.RED,Color.YELLOW,LKJ2000GuiComponents.LKJ_FONT_COMMON_BREAK,53);
        GuiLiner.batchDraw(graphics.bufferSource(),graphics.pose().last().pose(),layout_lines);
        GuiLiner.batchDraw(graphics.bufferSource(),graphics.pose().last().pose(),element_lines);
        poseStack.pushPose();
        poseStack.scale(0.5f,0.5f,1f);
        for(int i=0;i<=7;i++){
            String speedString = String.valueOf(i * 20);
            int textTop = (141 - i * 14) * 2 - font.lineHeight;
            int textLeft = (21 * 2) - font.width(speedString);
            graphics.drawString(font,speedString,textLeft,textTop,0xFF000000,false);
        }
        poseStack.popPose();
        int translatedTrainLength = 15;
        graphics.fill(68 - translatedTrainLength,138,67,140,Color.PURPLE.getColorCode());

        poseStack.pushPose();
        poseStack.scale(1.4f,2f,100);
        poseStack.translate(35f,5f,0);
        graphics.drawString(font, Component.literal(speedStr),18 - font.width(speedStr)+3,0,0x00ff00,false);
        graphics.drawString(font, Component.literal(distanceStr),24 - font.width(distanceStr) + 28 + 28 + 5 ,0,0xffff00,false);
        poseStack.translate(0.5f,0,0);
        graphics.drawString(font, Component.literal("0"),18 - font.width("0") + 28 ,0,0xff0000,false);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.scale(0.75f,0.75f,1);
        graphics.drawString(font, Component.literal(""),242,11,0xffffff,false);
        poseStack.scale(1f,1.25f,1);
        graphics.drawString(font, Component.literal("1.1451"),242,21,0xffff00,false);
        poseStack.popPose();
        graphics.renderItem(clockStack,229,10,12,12);
        poseStack.popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void renderStatus(GuiGraphics graphics, Color color, Color fontColor, GuiTexture text, int y){
        LKJ2000GuiComponents.LKJ_STATUS_LIGHT_WRAPPER.render(graphics,236,y);
        LKJ2000GuiComponents.LKJ_STATUS_LIGHT.render(graphics,color,236,y);
        text.render(graphics,fontColor,236+2,y+2,13,6);
    }
}