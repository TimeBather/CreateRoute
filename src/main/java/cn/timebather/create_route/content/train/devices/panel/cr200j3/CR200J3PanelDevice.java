package cn.timebather.create_route.content.train.devices.panel.cr200j3;

import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.*;
import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.common.networking.DevicePeer;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.api.CR200J3Client;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.api.CR200J3Server;
import cn.timebather.create_route.content.train.devices.panel.cr200j3.screen.CR200J3PanelScreen;
import cn.timebather.create_route.content.train.traction.TractionEngine;
import cn.timebather.create_route.interfaces.TrainTractionEngineProvider;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Consumer;

public class CR200J3PanelDevice extends TrainDevice implements ScreenDevice {

    Lazy<CR200J3Server> server = Lazy.of(()->new CR200J3Server(this));
    Lazy<CR200J3Client> client = Lazy.of(()->new CR200J3Client(this));
    private CarriageDeviceManager deviceManager;

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
        return AllTrainDevices.PANEL_CR200J3.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Screen createScreen() {
        return new CR200J3PanelScreen(this);
    }

    public void setPower(int power) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Operation","SetPower");
        tag.putInt("Power",power);
        this.packetSender.accept(this.id,tag);
    }

    @Override
    public void init(CarriageDeviceManager carriageDeviceManager) {
        this.deviceManager = carriageDeviceManager;
    }

    protected void executeIfEngine(Consumer<TractionEngine> tec){
        Train train = this.deviceManager.getCarriage().train;
        if(train == null)
            return;
        TrainTractionEngineProvider tep = (TrainTractionEngineProvider) train;
        TractionEngine engine = tep.getEngine();
        if(engine == null)
            return;
        tec.accept(engine);
    }

    public void setPowerServer(int power) {
        executeIfEngine((engine)->engine.setPower(power * 30));
    }

    public void setAirBreak(int val) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Operation","SetAirBreak");
        tag.putInt("AirBreak",val);
        this.packetSender.accept(this.id,tag);
    }

    public void setAirBreakServer(int airBreak) {
        executeIfEngine((engine)->engine.setBreak(airBreak / 30.0f));
    }
}
