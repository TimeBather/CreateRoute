package cn.timebather.create_route.content.train.devices;

import com.simibubi.create.content.trains.entity.CarriageContraption;

public class TrainDeviceType<T extends TrainDevice>{

    private final TrainDeviceSuppler<? extends T> factory;

    TrainDeviceType(TrainDeviceSuppler<? extends T> factory){
        this.factory = factory;
    }

    public T create(CarriageContraption contraption){
        return this.factory.create(contraption);
    }

    public static final class Builder<T extends TrainDevice>{
        private final TrainDeviceSuppler<? extends T> factory;
        public Builder(TrainDeviceSuppler<? extends T> factory){
            this.factory = factory;
        }

        public static <T extends TrainDevice> Builder<T> of(TrainDeviceSuppler<T> factory) {
            return new Builder<T>(factory);
        }

        public TrainDeviceType<T> build(){
            return new TrainDeviceType<T>(this.factory);
        }
    }


    @FunctionalInterface
    public interface TrainDeviceSuppler<T extends TrainDevice> {
        T create(CarriageContraption contraption);
    }
}
