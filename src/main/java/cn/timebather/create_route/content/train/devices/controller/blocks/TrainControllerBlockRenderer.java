package cn.timebather.create_route.content.train.devices.controller.blocks;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceProvider;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.DirectionalBlock;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.concurrent.atomic.AtomicBoolean;

public class TrainControllerBlockRenderer implements BlockEntityRenderer<TrainControllerBlockEntity> {
    GeoBlockRenderer<TrainControllerBlockEntity> defaultRenderer = new GeoBlockRenderer<>(new TrainControllerDefaultModel());

    public TrainControllerBlockRenderer(BlockEntityRendererProvider.Context context) {

    }

    public TrainControllerBlockRenderer() {

    }

    @Override
    public void render(TrainControllerBlockEntity trainControllerBlock, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int packedLight) {
        AtomicBoolean haveSRI = new AtomicBoolean(false);
        Direction direction = trainControllerBlock.getBlockState().hasProperty(DirectionalBlock.FACING) ?
                trainControllerBlock.getBlockState().getValue(DirectionalBlock.FACING) :
                Direction.NORTH;
        trainControllerBlock.getOptional().ifPresent((stackHandler)->{
            for (Item item : stackHandler.getItems()) {
                if(item instanceof TrainDeviceProvider deviceProvider){
                    TrainDeviceType<?> type = deviceProvider.getDevice();
                    haveSRI.set(true);
                    type.renderStatic(trainControllerBlock.getBlockPos(),direction,v,poseStack,multiBufferSource,i,packedLight);
                    // spi.render(trainControllerBlock,v,poseStack,multiBufferSource,i, packedLight);
                }
            }
        });
        if(!haveSRI.get())
            this.defaultRenderer.render(trainControllerBlock,v,poseStack,multiBufferSource,i,packedLight);
    }

    private static int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight,sLight);
    }
}
