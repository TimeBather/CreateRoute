package cn.timebather.create_route;

import cn.timebather.create_route.content.train.devices.controller.screens.TrainControllerStorageMenu;
import cn.timebather.create_route.content.train.devices.controller.screens.TrainControllerStorageScreen;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.timebather.create_route.CreateRoute.REGISTRATE;

public class AllMenuTypes {
    public static final MenuEntry<TrainControllerStorageMenu> TRAIN_CONTROLLER_STORAGE =
            REGISTRATE.menu("train_controller", TrainControllerStorageMenu::new, ()-> TrainControllerStorageScreen::new)
                    .register();
    public static void init(){

    }
}
