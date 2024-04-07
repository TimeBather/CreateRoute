package cn.timebather.create_route.content.train.devices.controller.blocks;

import cn.timebather.create_route.AllBlockEntities;
import cn.timebather.create_route.content.train.AllMovingBehaviours;
import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.MovingInteractiveBlock;
import cn.timebather.create_route.content.train.devices.TrainDeviceProvider;
import cn.timebather.create_route.content.train.devices.TrainDeviceType;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class TrainControllerBlock extends HorizontalDirectionalBlock implements TrainDeviceProvider, MovingInteractiveBlock, EntityBlock {
    public TrainControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TrainDeviceType<TrainControllerDevice> getDevice() {
        return AllTrainDevices.TRAIN_CONTROLLER.get();
    }

    @Override
    public MovementBehaviour getMovementBehaviour() {
        return null;
    }

    @Override
    public MovingInteractionBehaviour getMovingInteractionBehaviour() {
        return AllMovingBehaviours.TRAIN_CONTROLLER.get();
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if(!(player instanceof ServerPlayer serverPlayer))
            return InteractionResult.SUCCESS;
        NetworkHooks.openScreen((ServerPlayer) player, level.getBlockEntity(blockPos,AllBlockEntities.TRAIN_CONTROLLER.get()).orElseThrow(), (byteBuf)->byteBuf.writeBlockPos(blockPos));
        return InteractionResult.SUCCESS;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return AllBlockEntities.TRAIN_CONTROLLER.create(blockPos,blockState);
    }
}
