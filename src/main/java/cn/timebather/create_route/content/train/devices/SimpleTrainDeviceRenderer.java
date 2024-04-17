package cn.timebather.create_route.content.train.devices;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface SimpleTrainDeviceRenderer {
    @OnlyIn(Dist.CLIENT)
    public void render(TrainDevice parent,float v, PoseStack poseStack, MultiBufferSource multiBufferSource,int i,int i1);
}
