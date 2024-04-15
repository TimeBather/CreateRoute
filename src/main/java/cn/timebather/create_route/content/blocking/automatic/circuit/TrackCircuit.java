package cn.timebather.create_route.content.blocking.automatic.circuit;

import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.nbt.CompoundTag;

import java.util.Set;
import java.util.UUID;

public class TrackCircuit {
    UUID id;

    public Set<Train> trains;

    TrackCircuit(UUID id){
        this.id = id;
    }

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id",id);
        return tag;
    }

    public static TrackCircuit read(CompoundTag tag){
        return new TrackCircuit(tag.getUUID("Id"));
    }
}
