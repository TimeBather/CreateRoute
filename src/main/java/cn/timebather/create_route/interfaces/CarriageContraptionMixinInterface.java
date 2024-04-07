package cn.timebather.create_route.interfaces;

import net.minecraft.core.Direction;

public interface CarriageContraptionMixinInterface {
    public void createRouteRewrite$setForwardControl(boolean isForwardControls);

    public void createRouteRewrite$setBackwardControl(boolean isBackwardControls);
    public void createRouteRewrite$setSidewaysControl(boolean isSidewaysControls);
}
