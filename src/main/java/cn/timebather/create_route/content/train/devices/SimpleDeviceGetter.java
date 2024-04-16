package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.interfaces.CarriageMixinInterface;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;

import javax.annotation.Nullable;
import java.util.UUID;

public class SimpleDeviceGetter {

    public static @Nullable Carriage getCarriage(GlobalRailwayManager railway,UUID trainId, int carriageId){
        Train train = railway.trains.get(trainId);
        if(train == null)
            return null;
        return train.carriages.get(carriageId);
    }
    public static @Nullable CarriageDeviceManager getManager(Carriage carriage){
        if(!(carriage instanceof CarriageMixinInterface cmi))
            return null;
        return cmi.createRoute$getDeviceManager();
    }
}
