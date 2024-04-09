package cn.timebather.create_route.content.train;

import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.content.train.devices.TrainDeviceMovingBehavior;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AllMovingBehaviours {
    public static final DeferredRegister<TrainDeviceMovingBehavior> TRAIN_DEVICE_BEHAVIOUR =
            DeferredRegister.create(CreateRoute.asResource("train_device_behaviour"),CreateRoute.MODID);

    public static final Supplier<IForgeRegistry<TrainDeviceMovingBehavior>> REGISTRY =
            TRAIN_DEVICE_BEHAVIOUR.makeRegistry(RegistryBuilder::new);
    public static final RegistryObject<TrainDeviceMovingBehavior> TRAIN_CONTROLLER =
            TRAIN_DEVICE_BEHAVIOUR.register("train_controller",()->TrainDeviceMovingBehavior.create(AllTrainDevices.TRAIN_CONTROLLER.get()));

    public static void init(IEventBus eventBus){
        TRAIN_DEVICE_BEHAVIOUR.register(eventBus);
    }
}
