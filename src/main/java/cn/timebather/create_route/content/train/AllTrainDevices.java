package cn.timebather.create_route.content.train;

import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.TrainDeviceType;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.LKJ2000Device;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AllTrainDevices {
    public static final DeferredRegister<TrainDeviceType<? extends TrainDevice>> DEVICES =
            DeferredRegister.create(CreateRoute.asResource("train_device"),CreateRoute.MODID);

    public static final Supplier<IForgeRegistry<TrainDeviceType<? extends TrainDevice>>> REGISTRY =
            DEVICES.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<TrainDeviceType<TrainControllerDevice>> TRAIN_CONTROLLER =
            DEVICES.register("train_controller", ()-> TrainDeviceType.Builder.of(TrainControllerDevice::new).build());

    public static final RegistryObject<TrainDeviceType<LKJ2000Device>> LKJ_2000 =
            DEVICES.register("lkj2000", ()-> TrainDeviceType.Builder.of(LKJ2000Device::new).build());

    public static void init(IEventBus eventBus){
        DEVICES.register(eventBus);
    }
}
