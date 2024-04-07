package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.interfaces.DeviceCarriageContraption;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class TrainDeviceMovingBehavior extends SimpleBlockMovingInteraction {
    private final TrainDeviceType<?> trainDeviceType;

    TrainDeviceMovingBehavior(TrainDeviceType<?> trainDeviceType){
        this.trainDeviceType = trainDeviceType;
    }

    @Override
    protected BlockState handle(Player player, Contraption contraption, BlockPos blockPos, BlockState blockState) {
        if(!(contraption instanceof DeviceCarriageContraption carriageContraption)){
            return blockState;
        }

        ContraptionDeviceManager deviceManager = carriageContraption
                .createRouteRewrite$getDeviceManager();

        return
                deviceManager.devices
                .get(trainDeviceType)
                .interaction(player,deviceManager,contraption,blockPos,blockState);
    }

    public static TrainDeviceMovingBehavior create(TrainDeviceType<?> trainDeviceType){
        return new TrainDeviceMovingBehavior(trainDeviceType);
    }
}
