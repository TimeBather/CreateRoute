package cn.timebather.create_route.content.train.devices.common.rendering;

import cn.timebather.create_route.content.train.devices.TrainDevice;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.List;
import java.util.Objects;

public class TrainDeviceRenderer<T extends TrainDevice & GeoAnimatable> implements GeoRenderer<T>{
    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<T> model;
    protected T animatable;
    protected float scaleWidth = 1.0F;
    protected float scaleHeight = 1.0F;
    protected Matrix4f blockRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    public TrainDeviceRenderer(GeoModel<T> model) {
        this.model = model;
    }

    public GeoModel<T> getGeoModel() {
        return this.model;
    }

    public T getAnimatable() {
        return this.animatable;
    }

    public long getInstanceId(T animatable) {
        return (long)animatable.id.hashCode();
    }

    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public TrainDeviceRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);
        return this;
    }

    public TrainDeviceRenderer<T> withScale(float scale) {
        return this.withScale(scale, scale);
    }

    public TrainDeviceRenderer<T> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        return this;
    }

    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.blockRenderTranslations = new Matrix4f(poseStack.last().pose());
        this.scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    public void render(T animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.animatable = animatable;
        this.defaultRender(poseStack, this.animatable, bufferSource, (RenderType)null, (VertexConsumer)null, 0.0F, partialTick, packedLight);
    }

    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender) {
            AnimationState<T> animationState = new AnimationState(animatable, 0.0F, 0.0F, partialTick, false);
            long instanceId = this.getInstanceId(animatable);
            animationState.setData(DataTickets.TICK, ((GeoAnimatable)animatable).getTick(animatable));
            // animationState.setData(DataTickets.BLOCK_ENTITY, animatable);
            Objects.requireNonNull(animationState);
            getGeoModel().addAdditionalStateData(animatable, instanceId, animationState::setData);
            poseStack.translate(0.5, 0.0, 0.5);
            this.rotateBlock(this.getFacing(animatable), poseStack);
            this.model.handleAnimations(animatable, instanceId, animationState);
        }

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());
        GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
            Matrix4f worldState = new Matrix4f(localMatrix);
            BlockPos pos = this.animatable.getBlockPos();
            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(localMatrix);
            bone.setWorldSpaceMatrix(worldState.translate(new Vector3f((float)pos.getX(), (float)pos.getY(), (float)pos.getZ())));
        }

        GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void fireCompileRenderLayersEvent() {

    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {
        return false;
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {

    }

    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        switch (facing) {
            case SOUTH:
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                break;
            case WEST:
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                break;
            case NORTH:
                poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
                break;
            case EAST:
                poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                break;
            case UP:
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                break;
            case DOWN:
                poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
        }

    }

    protected Direction getFacing(T device) {
        return device.getFacing();
    }

    public void updateAnimatedTextureFrame(T animatable) {
        AnimatableTexture.setAndUpdate(this.getTextureLocation(animatable));
    }

}
