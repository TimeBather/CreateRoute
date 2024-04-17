package cn.timebather.create_route.content.train.devices.controller.blocks;

import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class TrainControllerBlockItemStackHandler extends ItemStackHandler {
    private final TrainControllerBlockEntity blockEntity;

    TrainControllerBlockItemStackHandler(TrainControllerBlockEntity blockEntity){
        super(27);
        this.blockEntity = blockEntity;
    }
    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem() instanceof TrainDeviceProvider;
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        blockEntity.setChanged();
    }

    public Item[] getItems(){
        return this.stacks.stream().map(ItemStack::getItem).collect(Collectors.toUnmodifiableSet()).toArray(Item[]::new);
    }
}
