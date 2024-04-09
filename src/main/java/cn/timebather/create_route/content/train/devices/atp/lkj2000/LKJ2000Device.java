package cn.timebather.create_route.content.train.devices.atp.lkj2000;

import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.*;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Client;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Server;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.screen.LKJ2000Screen;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;

public class LKJ2000Device extends TrainDevice implements ScreenDevice {
    public LKJ2000Device() {}


    Lazy<LKJ2000Server> server = Lazy.of(LKJ2000Server::new);
    Lazy<LKJ2000Client> client = Lazy.of(LKJ2000Client::new);

    @Override
    public DevicePeer getServer() {
        return server.get();
    }

    @Override
    public DevicePeer getClient() {
        return client.get();
    }

    @Override
    public TrainDeviceType<? extends TrainDevice> getType() {
        return AllTrainDevices.LKJ_2000.get();
    }

    @Override
    public Screen createScreen() {
        return new LKJ2000Screen(client.get(),this);
    }
}
