package cn.timebather.create_route.content.train.devices.controller.blocks;

import cn.timebather.create_route.AllBlockEntities;
import cn.timebather.create_route.content.train.AllMovingBehaviours;
import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.common.behaviours.MovingInteractiveBlock;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceProvider;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDeviceSpecialRenderMovingBehavior;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class TrainControllerBlock extends HorizontalDirectionalBlock implements TrainDeviceProvider, MovingInteractiveBlock, EntityBlock {
    public TrainControllerBlock(Properties properties) {
        super(properties.noOcclusion());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{FACING});
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return super.getStateForPlacement(placeContext)
                .setValue(FACING,placeContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public TrainDeviceType<TrainControllerDevice> getDevice() {
        return AllTrainDevices.TRAIN_CONTROLLER.get();
    }

    Lazy<TrainControllerDeviceSpecialRenderMovingBehavior> behaviorLazy =
            Lazy.of(TrainControllerDeviceSpecialRenderMovingBehavior::new);
    @Override
    public MovementBehaviour getMovementBehaviour() {
        return behaviorLazy.get();
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

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }



    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return AllBlockEntities.TRAIN_CONTROLLER.create(blockPos,blockState);
    }
}
