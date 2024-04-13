package cn.timebather.create_route.interfaces;

import cn.timebather.create_route.content.train.traction.TractionEngine;

public interface TrainTractionEngineProvider {
    TractionEngine getEngine();
    void setEngine(TractionEngine engine);
}
