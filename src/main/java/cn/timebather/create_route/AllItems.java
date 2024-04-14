package cn.timebather.create_route;

import cn.timebather.create_route.content.train.devices.atp.lkj2000.items.LKJ2000ControllerItem;
import cn.timebather.create_route.content.train.devices.controller.screens.TrainControllerStorageMenu;
import cn.timebather.create_route.content.train.devices.controller.screens.TrainControllerStorageScreen;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.item.CR200J3PanelItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.MenuEntry;

import static cn.timebather.create_route.CreateRoute.REGISTRATE;

public class AllItems {
    public static final ItemEntry<LKJ2000ControllerItem> LKJ_2000 =
            REGISTRATE.item("atp_lkj2000", LKJ2000ControllerItem::new)
                    .register();

    public static final ItemEntry<CR200J3PanelItem> PANEL_CR200J3 =
            REGISTRATE.item("panel_cr200j3", CR200J3PanelItem::new)
                    .register();


    public static void init(){}
}
