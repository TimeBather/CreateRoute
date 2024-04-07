package cn.timebather.create_route.content.station.screen.board_port;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

public class BoardPortScreenEditPacket extends SimplePacketBase {

    BlockPos blockPos;
    String leftPortId;
    String rightPortId;

    public BoardPortScreenEditPacket(FriendlyByteBuf friendlyByteBuf){
        this.blockPos = friendlyByteBuf.readBlockPos();
        CompoundTag compoundTag = friendlyByteBuf.readNbt();
        if(compoundTag == null)
            return;
        leftPortId = compoundTag.getString("leftPortId");
        rightPortId = compoundTag.getString("rightPortId");
    }

    public BoardPortScreenEditPacket(BlockPos blockPos,String leftPortId, String rightPortId){
        this.blockPos = blockPos;
        this.leftPortId = leftPortId;
        this.rightPortId = rightPortId;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(blockPos);
        CompoundTag tag = new CompoundTag();
        tag.putString("leftPortId",leftPortId);
        tag.putString("rightPortId",rightPortId);
        friendlyByteBuf.writeNbt(tag);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(()->{
            Level level = context.getSender().level();
            if(!level.hasChunkAt(blockPos)){
                return;
            }
            BlockState state = level.getBlockState(blockPos);
            if(level.getBlockEntity(blockPos) instanceof BoardPortScreenBlockEntity boardPortScreenBlockEntity){
                boardPortScreenBlockEntity.rightPortId = rightPortId;
                boardPortScreenBlockEntity.leftPortId = leftPortId;
                boardPortScreenBlockEntity.setChanged();
                level.sendBlockUpdated(blockPos,state,state, Block.UPDATE_ALL);
            }
        });
        return true;
    }
}
