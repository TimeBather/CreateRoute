package cn.timebather.create_route.content.train.devices;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface AbstractTrainDeviceRenderer<T> {
    @OnlyIn(Dist.CLIENT)
    public void render(T parent,float v, PoseStack poseStack, MultiBufferSource multiBufferSource,int i,int i1);

    @OnlyIn(Dist.CLIENT)
    public void renderBlock(BlockPos pos, Direction direction, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay);
}
