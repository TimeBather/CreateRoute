package cn.timebather.create_route.content.train.devices;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.content.train.devices.common.manager.ContraptionDeviceManager;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.packets.DeviceInteractionResultPacket;
import cn.timebather.create_route.interfaces.CarriageMixinInterface;
import cn.timebather.create_route.interfaces.DeviceCarriageContraption;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class TrainDeviceMovingBehavior extends SimpleBlockMovingInteraction {
    private final TrainDeviceType<?> trainDeviceType;

    TrainDeviceMovingBehavior(TrainDeviceType<?> trainDeviceType){
        this.trainDeviceType = trainDeviceType;
    }

    @Override
    protected BlockState handle(Player player, Contraption contraption, BlockPos blockPos, BlockState blockState) {
        if((!(contraption instanceof CarriageContraption carriageContraption))){
            return blockState;
        }

        DeviceCarriageContraption locationProvider = (DeviceCarriageContraption) carriageContraption;

        Level level = player.level();

        if(level.isClientSide)
            return blockState;

        ContraptionDeviceManager deviceManager = locationProvider
                .createRouteRewrite$getDeviceManager();

        CarriageContraptionEntity entity = ((CarriageContraptionEntity)contraption.entity);

        UUID trainId = entity.trainId;

        Train train = Create.RAILWAYS.trains.get(trainId);

        Carriage carriage = entity.getCarriage();

        int carriageId = train.carriages.indexOf(carriage);

        UUID deviceId = deviceManager.getLocators(blockPos);

        if(deviceId == null)
            return blockState;

        TrainDevice device = ((CarriageMixinInterface)carriage).createRoute$getDeviceManager().getDevice(deviceId);

        ResourceLocation deviceType = AllTrainDevices.REGISTRY.get().getKey(device.getType());

        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(()->(ServerPlayer)player),
                new DeviceInteractionResultPacket(trainId,carriageId,deviceId,deviceType,device.write()));

        return blockState;
    }

    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        super.handlePlayerInteraction(player, activeHand, localPos, contraptionEntity);
        return true;
    }

    public static TrainDeviceMovingBehavior create(TrainDeviceType<?> trainDeviceType){
        return new TrainDeviceMovingBehavior(trainDeviceType);
    }
}
