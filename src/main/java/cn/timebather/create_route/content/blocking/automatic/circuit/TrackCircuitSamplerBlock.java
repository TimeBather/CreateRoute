package cn.timebather.create_route.content.blocking.automatic.circuit;

import cn.timebather.create_route.AllBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TrackCircuitSamplerBlock extends Block implements IBE<TrackCircuitSamplerBlockEntity> {
    public TrackCircuitSamplerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<TrackCircuitSamplerBlockEntity> getBlockEntityClass() {
        return TrackCircuitSamplerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TrackCircuitSamplerBlockEntity> getBlockEntityType() {
        return AllBlockEntities.TRACK_CIRCUIT_SAMPLER.get();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state,level,pos,newState);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level level, BlockPos blockPos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if(!(be instanceof TrackCircuitSamplerBlockEntity tcsbe)){
            return InteractionResult.FAIL;
        }
        if(level.isClientSide)
            return InteractionResult.SUCCESS;
        tcsbe.displayGroupIds(player);
        return InteractionResult.SUCCESS;
    }
}
