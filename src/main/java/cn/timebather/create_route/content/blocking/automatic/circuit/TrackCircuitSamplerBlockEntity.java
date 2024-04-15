package cn.timebather.create_route.content.blocking.automatic.circuit;

import cn.timebather.create_route.AllEdgeTypes;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TrackCircuitSamplerBlockEntity extends SmartBlockEntity {

    public TrackTargetingBehaviour<TrackCircuitBoundary> edgePoint;

    public TrackCircuitSamplerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState state) {
        super(type, blockPos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(edgePoint = new TrackTargetingBehaviour<>(this, AllEdgeTypes.TRACK_CIRCUIT_BOUNDARY));
    }

    public void displayGroupIds(Player player){
        TrackEdgePoint edgePoint = this.edgePoint.getEdgePoint();
        if(edgePoint instanceof TrackCircuitBoundary tcb){
            player.sendSystemMessage(Component.literal("FALSE->"+tcb.sidedCircuits.get(false).toString()+",TRUE->"+tcb.sidedCircuits.get(true).toString()));
        }
    }
}
