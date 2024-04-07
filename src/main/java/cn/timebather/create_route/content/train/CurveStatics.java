package cn.timebather.create_route.content.train;

public class CurveStatics {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    double average;
    double total;

    int size;

    void consume(double i){
        this.total += i;
        if(i < this.min)
            this.min = i;
        if(i > this.max)
            this.max = i;
        this.size ++;
    }

    void finish(){
        this.average = this.total / this.size;
    }
}
