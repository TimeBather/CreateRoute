package cn.timebather.create_route.content.train.devices.controller.blocks;

import cn.timebather.create_route.CreateRoute;
import cn.timebather.create_route.content.train.devices.controller.TrainControllerDevice;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class TrainControllerDefaultModel extends GeoModel<TrainControllerBlockEntity> {
    ResourceLocation MODEL = CreateRoute.asResource("geo/controller_empty.geo.json");
    ResourceLocation TEXTURE = CreateRoute.asResource("textures/geo/controller_empty.png");


    @Override
    public ResourceLocation getModelResource(TrainControllerBlockEntity geoAnimatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(TrainControllerBlockEntity geoAnimatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(TrainControllerBlockEntity geoAnimatable) {
        return null;
    }
}
