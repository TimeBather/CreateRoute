package cn.timebather.create_route.content.train;

public abstract class SpeedCurve {

    protected int tick = 0;

    abstract public double next();

    public CurveStatics next(int n){
        CurveStatics statics = new CurveStatics();
        for (int i = 0; i < n; i++) {
            statics.consume(this.next());
        }
        statics.finish();
        return statics;
    }
}
