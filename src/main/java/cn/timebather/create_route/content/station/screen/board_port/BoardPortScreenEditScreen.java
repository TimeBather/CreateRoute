package cn.timebather.create_route.content.station.screen.board_port;

import cn.timebather.create_route.AllPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class BoardPortScreenEditScreen extends Screen {
    protected BoardPortScreenEditScreen(BoardPortScreenBlockEntity be) {
        super(Component.literal(""));
        EditBox left = new EditBox(Minecraft.getInstance().font, 0, 0, 60, 20, Component.literal(""));
        EditBox right = new EditBox(Minecraft.getInstance().font, 0, 20, 60, 20, Component.literal(""));
        Button ok = Button.builder(
                Component.literal("OK"),
                (e)->{
                    AllPackets.getChannel().sendToServer(new BoardPortScreenEditPacket(be.getBlockPos(),left.getValue(),right.getValue()));
                    this.onClose();
                }
        ).pos(0,40).size(60,20).build();
        left.setValue(be.leftPortId);
        right.setValue(be.rightPortId);
        this.addRenderableWidget(left);
        this.addRenderableWidget(right);
        this.addRenderableWidget(ok);
        this.width = 100;
        this.height = 100;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ticks) {
        super.render(guiGraphics, mouseX, mouseY, ticks);
    }
}
