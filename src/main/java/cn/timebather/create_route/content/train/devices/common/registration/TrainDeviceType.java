package cn.timebather.create_route.content.train.devices.common.registration;

import cn.timebather.create_route.content.train.devices.AbstractTrainDeviceRenderer;
import cn.timebather.create_route.content.train.devices.TrainDevice;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class TrainDeviceType<T extends TrainDevice>{

    private final TrainDeviceSuppler<? extends T> factory;
    private final Supplier<TrainDeviceRendererBuilder> renderer;
    private final Supplier<TrainDeviceScreenBuilder<T>> screenBuilder;

    TrainDeviceType(TrainDeviceSuppler<? extends T> factory, Supplier<TrainDeviceRendererBuilder> renderer, Supplier<TrainDeviceScreenBuilder<T>> screenBuilder){
        this.factory = factory;
        this.renderer = renderer;
        this.screenBuilder = screenBuilder;
    }

    public T create(){
        return this.factory.create();
    }

    public Supplier<TrainDeviceRendererBuilder> getRendererBuilder(){
        return renderer;
    }

    public Supplier<TrainDeviceScreenBuilder<T>> getScreenBuilder(){
        return screenBuilder;
    }

    public void openScreen(T device, Carriage carriage){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            Minecraft.getInstance().setScreen(this.screenBuilder.get().create(device,carriage));
        });
    }

    @OnlyIn(Dist.CLIENT)
    public Screen createScreenInstance(T device){
        return this.screenBuilder.get().create(device,device.getCarriage());
    }

    public AbstractTrainDeviceRenderer<T> createRendererInstance(){
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT,()->()->{
            if(this.renderer == null)
                return null;
            TrainDeviceRendererBuilder rendererSupplier = this.renderer.get();
            if(rendererSupplier == null)
                return null;
            @SuppressWarnings("unchecked")
            AbstractTrainDeviceRenderer<T> renderer = (AbstractTrainDeviceRenderer<T>)rendererSupplier.create();
            return renderer;
        });
    }

    private final Lazy<AbstractTrainDeviceRenderer<T>> rendererInstance = Lazy.of(this::createRendererInstance);

    public boolean renderCast(TrainDevice device, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1){
        return render((T) device,v,poseStack,multiBufferSource,i,i1);
    }

    public boolean render(T device, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1){
        AbstractTrainDeviceRenderer<T> renderer = this.rendererInstance.get();
        if(renderer == null)
            return false;
        renderer.render(device,v,poseStack,multiBufferSource,i,i1);
        return true;
    }

    public boolean renderStatic(BlockPos pos, Direction direction, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        AbstractTrainDeviceRenderer<T> renderer = this.rendererInstance.get();
        if(renderer == null)
            return false;
        renderer.renderBlock(pos,direction,v, poseStack, multiBufferSource, i, i1);
        return true;
    }

    public boolean hasRenderer() {
        return this.renderer != null;
    }

    public static final class Builder<T extends TrainDevice>{
        private final TrainDeviceSuppler<? extends T> factory;
        private Supplier<TrainDeviceRendererBuilder> renderer;
        private Supplier<TrainDeviceScreenBuilder<T>> screenBuilder;

        public Builder(TrainDeviceSuppler<? extends T> factory){
            this.factory = factory;
        }

        public static <T extends TrainDevice> Builder<T> of(TrainDeviceSuppler<T> factory) {
            return new Builder<T>(factory);
        }

        public Builder<T> renderer(Supplier<TrainDeviceRendererBuilder> renderer){
            this.renderer = renderer;
            return this;
        }

        public TrainDeviceType<T> build(){
            return new TrainDeviceType<T>(this.factory,this.renderer,this.screenBuilder);
        }

        public Builder<T> screen(Supplier<TrainDeviceScreenBuilder<T>> screenBuilder){
            this.screenBuilder = screenBuilder;
            return this;
        }
    }

    @FunctionalInterface
    public interface TrainDeviceSuppler<T extends TrainDevice> {
        T create();
    }
}
