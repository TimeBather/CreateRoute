package cn.timebather.create_route.content.station.screen.board_port;

import cn.timebather.create_route.AllPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class BoardPortScreenEditScreen extends Screen {
    private final BoardPortScreenBlockEntity be;

    public BoardPortScreenEditScreen(BoardPortScreenBlockEntity be) {
        super(Component.literal(""));
        this.be = be;
    }

    @Override
    protected void init() {
        int contentWidth = 160;
        int contentHeight = 120;
        int centerBoxX = (width - contentWidth)/2;
        int centerBoxY = (height - contentHeight)/2;

        EditBox left = new EditBox(Minecraft.getInstance().font, centerBoxX + 20, centerBoxY + 40, 60, 15, Component.literal(""));
        EditBox right = new EditBox(Minecraft.getInstance().font, centerBoxX + 100, centerBoxY + 40, 60, 15, Component.literal(""));
        StringWidget leftTitle = new StringWidget(centerBoxX + 20,centerBoxY + 25,60,15,Component.literal("左侧进站口编号"),font);
        StringWidget rightTitle = new StringWidget(centerBoxX + 100,centerBoxY + 25,60,15,Component.literal("右侧进站口编号"),font);
        Button ok = Button.builder(
                Component.literal("OK"),
                (e)->{
                    AllPackets.getChannel().sendToServer(new BoardPortScreenEditPacket(be.getBlockPos(),left.getValue(),right.getValue()));
                    this.onClose();
                }
        ).pos(centerBoxX + 20,centerBoxY + 95).size(140,15).build();
        left.setValue(be.leftPortId);
        right.setValue(be.rightPortId);
        this.addRenderableWidget(left);
        this.addRenderableWidget(right);
        this.addRenderableWidget(ok);
        this.addRenderableOnly(leftTitle);
        this.addRenderableOnly(rightTitle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ticks) {
        super.render(guiGraphics, mouseX, mouseY, ticks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
