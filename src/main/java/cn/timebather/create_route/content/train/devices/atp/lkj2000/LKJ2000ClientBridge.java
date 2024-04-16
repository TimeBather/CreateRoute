package cn.timebather.create_route.content.train.devices.atp.lkj2000;

import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Client;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.screen.LKJ2000Screen;

public class LKJ2000ClientBridge {
    public static LKJ2000Screen createScreen(LKJ2000Client client, LKJ2000Device device){
        return new LKJ2000Screen(client,device);
    }
}
