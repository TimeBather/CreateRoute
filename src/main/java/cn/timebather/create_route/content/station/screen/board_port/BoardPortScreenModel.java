package cn.timebather.create_route.content.station.screen.board_port;

import cn.timebather.create_route.CreateRoute;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoardPortScreenModel extends GeoModel<BoardPortScreenBlockEntity> {

    static ResourceLocation MODEL_RESOURCE = CreateRoute.asResource("geo/station/board_port_screen.geo.json");
    static ResourceLocation TEXTURE_RESOURCE = CreateRoute.asResource("textures/geo/station/board_port_screen.png");
    @Override
    public ResourceLocation getModelResource(BoardPortScreenBlockEntity boardPortScreenBlockEntity) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(BoardPortScreenBlockEntity boardPortScreenBlockEntity) {
        return TEXTURE_RESOURCE;
    }

    @Override
    public ResourceLocation getAnimationResource(BoardPortScreenBlockEntity boardPortScreenBlockEntity) {
        return null;
    }
}
