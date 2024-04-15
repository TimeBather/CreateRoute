package cn.timebather.create_route;

import cn.timebather.create_route.content.blocking.automatic.circuit.TrackCircuitSamplerBlock;
import cn.timebather.create_route.content.station.screen.board_port.BoardPortScreen;
import cn.timebather.create_route.content.train.devices.controller.blocks.TrainControllerBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntry;

import static cn.timebather.create_route.CreateRoute.REGISTRATE;

public class AllBlocks {
    public static final BlockEntry<TrainControllerBlock> TRAIN_CONTROLLER = REGISTRATE.block("train_controller_block",TrainControllerBlock::new)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .build()
            .register();

    public static final BlockEntry<BoardPortScreen> ENTRY_PORT_SCREEN = REGISTRATE.block("entry_port_screen", BoardPortScreen::new)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .build()
            .register();

    public static final BlockEntry<TrackCircuitSamplerBlock> TRACK_CIRCUIT_SAMPLER = REGISTRATE.block("track_circuit_sampler", TrackCircuitSamplerBlock::new)
            .item(TrackTargetingBlockItem.ofType(AllEdgeTypes.TRACK_CIRCUIT_BOUNDARY))
            .build()
            .register();

    public static void init(){

    }
}
