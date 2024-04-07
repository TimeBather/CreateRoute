package cn.timebather.create_route.content.train.devices.controller.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TrainControllerStorageScreen extends AbstractContainerScreen<TrainControllerStorageMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");

    public TrainControllerStorageScreen(TrainControllerStorageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, ticks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int realWidth = (this.width - this.imageWidth) / 2;
        int realHeight = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CONTAINER_BACKGROUND, realWidth, realHeight, 0, 0, this.imageWidth, 3 * 18 + 17);
        guiGraphics.blit(CONTAINER_BACKGROUND, realWidth, realHeight + 3 * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
