package cn.timebather.create_route.content.train;

import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.LKJ2000Device;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.screen.LKJ2000Screen;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlockRenderer;
import cn.timebather.create_route.content.train.devices.controller.render.TrainControllerDeviceRenderer;
import cn.timebather.create_route.content.train.devices.controller.screens.TrainControllerControlScreen;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3GeoRenderer;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.CR200J3PanelDevice;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.screen.CR200J3PanelScreen;
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
            DEVICES.register("train_controller", ()->
                    TrainDeviceType.Builder.of(TrainControllerDevice::new)
                            .screen(()-> TrainControllerControlScreen::new)
                            .renderer(()-> TrainControllerDeviceRenderer::new)
                            .build()
            );

    public static final RegistryObject<TrainDeviceType<LKJ2000Device>> LKJ_2000 =
            DEVICES.register("atp_lkj2000", ()->
                    TrainDeviceType.Builder.of(LKJ2000Device::new)
                            .screen(() -> LKJ2000Screen::new)
                            .build()
            );

    public static final RegistryObject<TrainDeviceType<CR200J3PanelDevice>> PANEL_CR200J3 =
            DEVICES.register("panel_cr200j3",
                    ()-> TrainDeviceType.Builder.of(CR200J3PanelDevice::new)
                            .screen(()-> CR200J3PanelScreen::new)
                            .renderer(()-> CR200J3GeoRenderer::new)
                            .build()
            );

    public static void init(IEventBus eventBus){
        DEVICES.register(eventBus);
    }
}
