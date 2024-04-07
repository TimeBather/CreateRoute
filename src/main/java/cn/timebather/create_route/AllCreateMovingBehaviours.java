package cn.timebather.create_route;

import cn.timebather.create_route.content.train.devices.MovingInteractiveBlock;
import com.simibubi.create.AllInteractionBehaviours;
import com.simibubi.create.AllMovementBehaviours;

public class AllCreateMovingBehaviours {
    static void init(){
        AllMovementBehaviours.registerBehaviourProvider((state)->{
            if(state.getBlock() instanceof MovingInteractiveBlock movingInteractiveBlock)
                return movingInteractiveBlock.getMovementBehaviour();
            return null;
        });

        AllInteractionBehaviours.registerBehaviourProvider(state-> {
            if(state.getBlock() instanceof MovingInteractiveBlock movingInteractiveBlock)
                return movingInteractiveBlock.getMovingInteractionBehaviour();
            return null;
        });
    }
}
