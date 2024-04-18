package cn.timebather.create_route.content.station.screen.board_port;

import cn.timebather.create_route.AllBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

public class BoardPortScreen extends HorizontalDirectionalBlock implements EntityBlock {

    public BoardPortScreen(Properties p_54120_) {
        super(p_54120_.noOcclusion());
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return AllBlockEntities.ENTRY_PORT_SCREEN.create(blockPos,blockState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{FACING});
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return super.getStateForPlacement(placeContext)
                .setValue(FACING,placeContext.getHorizontalDirection());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(!level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(blockPos);

        if(be instanceof BoardPortScreenBlockEntity boardPortScreenBlockEntity){
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->BoardPortScreenScreenLoader.open(boardPortScreenBlockEntity));
        }
        return InteractionResult.SUCCESS;
    }
}
