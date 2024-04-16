package cn.timebather.create_route.content.train.devices.atp.lkj2000;

import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.*;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Client;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Server;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.screen.LKJ2000Screen;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Queue;

public class LKJ2000Device extends TrainDevice {
    @Nullable
    private Carriage carriage;

    public LKJ2000Device() {}


    Lazy<LKJ2000Server> server = Lazy.of(LKJ2000Server::new);
    Lazy<LKJ2000Client> client = Lazy.of(LKJ2000Client::new);

    @Override
    public LKJ2000Server getServer() {
        return server.get();
    }

    @Override
    public LKJ2000Client getClient() {
        return client.get();
    }

    @Override
    public TrainDeviceType<? extends TrainDevice> getType() {
        return AllTrainDevices.LKJ_2000.get();
    }

    @Override
    public void init(CarriageDeviceManager carriageDeviceManager) {
        this.carriage = carriageDeviceManager.getCarriage();
    }

    int perDistanceTick = 0;
    double lastDistance = 0;

    double lastRecordSpeed = 0;

    double recordedDistance = 0;

    ArrayList<Pair<Double,Double>> lastSpeedCurve = new ArrayList<>();

    @Override
    public void tick() {
        if(this.carriage == null || this.carriage.train == null)
            return;
        double speed = this.carriage.train.speed;
        if(lastDistance > 11.9045d || Math.abs(lastRecordSpeed - speed) > 5 * 20.0d || (speed == 0 && lastRecordSpeed != 0)){
            double averageSpeed = lastDistance / perDistanceTick * 20.0d * 3.6;
            this.lastSpeedCurve.add(Pair.of(averageSpeed,lastDistance));
            this.recordedDistance += lastDistance;
            while(recordedDistance >= 1000){
                Pair<Double,Double> last = this.lastSpeedCurve.remove(0);
                if(last != null){
                    recordedDistance -=  last.getSecond();
                }else{
                    recordedDistance = 0;
                }
            }
            this.lastDistance = 0;
            this.perDistanceTick = 0;
            this.lastRecordSpeed = averageSpeed;
        }
        lastDistance += speed;
        perDistanceTick ++;
    }

    public ArrayList<Pair<Double,Double>> getLastSpeedCurve() {
        return lastSpeedCurve;
    }

    public Carriage getCarriage() {
        return carriage;
    }

    public double getRecordedDistance() {
        return recordedDistance;
    }
}
