package cn.timebather.create_route.content.train.devices;

import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;

public interface MovingInteractiveBlock {
    MovementBehaviour getMovementBehaviour();

    MovingInteractionBehaviour getMovingInteractionBehaviour();
}