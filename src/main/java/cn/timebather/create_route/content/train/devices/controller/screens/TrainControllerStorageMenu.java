package cn.timebather.create_route.content.train.devices.controller.screens;

import cn.timebather.create_route.AllBlocks;
import cn.timebather.create_route.AllMenuTypes;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class TrainControllerStorageMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess levelAccess;
    private TrainControllerBlockEntity blockEntity;

    public TrainControllerStorageMenu(MenuType<TrainControllerStorageMenu> type, int containerId, Inventory inventory, FriendlyByteBuf byteBuf){
        this(type, containerId, inventory, inventory.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    public TrainControllerStorageMenu(MenuType<TrainControllerStorageMenu> type, int containerId, Inventory inventory, BlockEntity blockEntity){
        super(AllMenuTypes.TRAIN_CONTROLLER_STORAGE.get(), containerId);
        if(!(blockEntity instanceof TrainControllerBlockEntity trainControllerBlock)){
            throw new IllegalStateException("Invaild BE Type: "+blockEntity.toString());
        }
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(),blockEntity.getBlockPos());
        this.createSlots(inventory,trainControllerBlock);
        this.blockEntity = trainControllerBlock;
    }

    private void createSlots(Inventory inventory,TrainControllerBlockEntity be) {

        be.getOptional().ifPresent((slot)->{
            int i,j;
            for(i = 0; i < 3; ++i) {
                for(j = 0; j < 9; ++j) {
                    this.addSlot(new SlotItemHandler(slot,  j + i * 9, 8 +  j * 18, 18 + i * 18));
                }
            }
        });

        int i,j;
        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory,  j + i * 9 + 9, 8 +  j * 18, 103 + i * 18 - 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 161 - 18));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, AllBlocks.TRAIN_CONTROLLER.get());
    }
}
