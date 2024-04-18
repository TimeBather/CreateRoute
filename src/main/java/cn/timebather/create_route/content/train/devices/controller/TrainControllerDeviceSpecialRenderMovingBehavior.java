package cn.timebather.create_route.content.train.devices.controller;

import cn.timebather.create_route.AllPackets;
import cn.timebather.create_route.content.train.devices.*;
import cn.timebather.create_route.content.train.devices.common.manager.CarriageDeviceManager;
import cn.timebather.create_route.content.train.devices.common.manager.ContraptionDeviceManager;
import cn.timebather.create_route.content.train.devices.common.registration.TrainDeviceType;
import cn.timebather.create_route.content.train.packets.DeviceConfigureRequestPacket;
import cn.timebather.create_route.interfaces.CarriageMixinInterface;
import cn.timebather.create_route.interfaces.DeviceCarriageContraption;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.render.BlockEntityRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.UUID;

public class TrainControllerDeviceSpecialRenderMovingBehavior implements MovementBehaviour {
    @Override
    public boolean isActive(MovementContext context) {
        return true;
    }

    @Override
    public boolean mustTickWhileDisabled() {
        return true;
    }

    @Nullable
    @Override
    public ItemStack canBeDisabledVia(MovementContext context) {
        return null;
    }

    @Override
    public void tick(MovementContext context) {
        if(!context.world.isClientSide)
            return;
        if(!(context.contraption.entity instanceof CarriageContraptionEntity entity))
            return;
        Carriage carriage = entity.getCarriage();
        CarriageMixinInterface cmi = (CarriageMixinInterface) carriage;
        CarriageDeviceManager cdm = cmi.createRoute$getDeviceManager();
        DeviceCarriageContraption locationProvider = (DeviceCarriageContraption) context.contraption;
        ContraptionDeviceManager deviceManager = locationProvider
                .createRouteRewrite$getDeviceManager();
        UUID deviceId = deviceManager.getLocators(context.localPos);
        if(cdm.isFetching(deviceId))
            return;
        cdm.markFetching(deviceId);
        AllPackets.getChannel().sendToServer(new DeviceConfigureRequestPacket(entity.trainId,carriage.id,deviceId));
    }

    @OnlyIn(Dist.CLIENT)
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        PoseStack ms = matrices.getModelViewProjection();
        ms.pushPose();
        TransformStack.cast(ms)
                .translate(context.localPos);
        int worldLight = ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld);
        int combinedLight = BlockEntityRenderHelper.getCombinedLight(context.world, getLightPos(matrices.getLight(),context.localPos),renderWorld,context.localPos);
        this.actuallyRender(context,ms,buffer,combinedLight);
        ms.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    private void actuallyRender(MovementContext context, PoseStack ms, MultiBufferSource buffer, int combinedLight) {
        if(!(context.contraption.entity instanceof CarriageContraptionEntity entity))
            return;
        CarriageMixinInterface cmi = (CarriageMixinInterface) entity.getCarriage();
        CarriageDeviceManager cdm = cmi.createRoute$getDeviceManager();
        DeviceCarriageContraption locationProvider = (DeviceCarriageContraption) context.contraption;
        ContraptionDeviceManager deviceManager = locationProvider
                .createRouteRewrite$getDeviceManager();
        UUID deviceId = deviceManager.getLocators(context.localPos);
        TrainDevice device = cdm.getDevice(deviceId);
        if(device == null)
            return;
        TrainDeviceType<?> deviceType = device.getType();
        deviceType.renderCast(device,0f,ms,buffer,combinedLight, OverlayTexture.NO_OVERLAY);
    }

    private static BlockPos getLightPos(@Nullable Matrix4f lightTransform, BlockPos contraptionPos) {
        if (lightTransform != null) {
            Vector4f lightVec = new Vector4f(contraptionPos.getX() + .5f, contraptionPos.getY() + .5f, contraptionPos.getZ() + .5f, 1);
            lightVec.mul(lightTransform);
            return BlockPos.containing(lightVec.x(), lightVec.y(), lightVec.z());
        } else {
            return contraptionPos;
        }
    }
}
