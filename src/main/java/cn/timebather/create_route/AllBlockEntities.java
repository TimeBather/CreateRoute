package cn.timebather.create_route;

import cn.timebather.create_route.content.blocking.automatic.circuit.TrackCircuitSamplerBlockEntity;
import cn.timebather.create_route.content.station.screen.board_port.BoardPortScreenBlockEntity;
import cn.timebather.create_route.content.station.screen.board_port.BoardPortScreenRenderer;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static cn.timebather.create_route.CreateRoute.REGISTRATE;

public class AllBlockEntities {
    public static final BlockEntityEntry<TrainControllerBlockEntity> TRAIN_CONTROLLER = REGISTRATE
            .blockEntity("train_controller",TrainControllerBlockEntity::new)
            .validBlocks(AllBlocks.TRAIN_CONTROLLER)
            .register();

    public static final BlockEntityEntry<BoardPortScreenBlockEntity> ENTRY_PORT_SCREEN = REGISTRATE
            .blockEntity("entry_port_screen", BoardPortScreenBlockEntity::new)
            .validBlocks(AllBlocks.ENTRY_PORT_SCREEN)
            .renderer(() -> BoardPortScreenRenderer::new)
            .register();

    public static final BlockEntityEntry<TrackCircuitSamplerBlockEntity> TRACK_CIRCUIT_SAMPLER = REGISTRATE
            .blockEntity("track_circuit_sampler", TrackCircuitSamplerBlockEntity::new)
            .validBlocks(AllBlocks.TRACK_CIRCUIT_SAMPLER)
            .register();

    public static void init(){

    }
}
