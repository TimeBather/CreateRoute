package cn.timebather.create_route.content.train.devices.atp.lkj2000;

import cn.timebather.create_route.content.train.devices.ContraptionDeviceManager;
import cn.timebather.create_route.content.train.devices.DevicePeer;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Client;
import cn.timebather.create_route.content.train.devices.atp.lkj2000.api.LKJ2000Server;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;

public class LKJ2000Device extends TrainDevice {
    public LKJ2000Device(CarriageContraption contraption) {
        super(contraption);
    }

    @Override
    public void onCapture(BlockState blockState, BlockPos pos, CarriageContraption carriageContraption, BlockEntity blockEntity) {

    }

    @Override
    public void read(CompoundTag tag) {

    }

    @Override
    public CompoundTag write() {
        return null;
    }

    @Override
    public void init(ContraptionDeviceManager contraptionDeviceManager) {

    }

    @Override
    public void dispose(ContraptionDeviceManager contraptionDeviceManager) {

    }

    @Override
    public void assembleCheck(ContraptionDeviceManager contraptionDeviceManager) {

    }

    @Override
    public BlockState interaction(Player player, ContraptionDeviceManager deviceManager, Contraption contraption, BlockPos blockPos, BlockState blockState) {
        return null;
    }

    Lazy<DevicePeer> server = Lazy.of(LKJ2000Server::new);
    Lazy<DevicePeer> client = Lazy.of(LKJ2000Client::new);

    @Override
    public DevicePeer getServer() {
        return server.get();
    }

    @Override
    public DevicePeer getClient() {
        return client.get();
    }
}
