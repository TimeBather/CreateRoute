package cn.timebather.create_route.content.train.devices.controller.render;

import cn.timebather.create_route.content.train.devices.AbstractTrainDeviceRenderer;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.apache.commons.lang3.NotImplementedException;

public class TrainControllerDeviceRenderer implements AbstractTrainDeviceRenderer<TrainControllerDevice> {
    @Override
    public void render(TrainControllerDevice parent, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        parent.getSubDevices().forEach((device)->{
            TrainDeviceType<?> deviceType = device.getType();
            if(deviceType.hasRenderer()){
                deviceType.renderCast(device,v,poseStack,multiBufferSource,i,i1);
            }
        });
    }

    @Override
    public void renderBlock(BlockPos pos, Direction direction, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        throw new NotImplementedException();
    }
}
