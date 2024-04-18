package cn.timebather.create_route.content.train.devices.panel.cr200j3;

import cn.timebather.create_route.CreateRoute;
import com.simibubi.create.Create;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class CR200J3Model extends GeoModel<CR200J3PanelDevice> {

    private static final ResourceLocation MODEL = CreateRoute.asResource("geo/panel/cr200j3/desktop.geo.json");
    private static final ResourceLocation TEXTURE = CreateRoute.asResource("textures/geo/panel/cr200j3/desktop.png");

    @Override
    public ResourceLocation getModelResource(CR200J3PanelDevice panelDevice) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CR200J3PanelDevice panelDevice) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CR200J3PanelDevice panelDevice) {
        return null;
    }
}
