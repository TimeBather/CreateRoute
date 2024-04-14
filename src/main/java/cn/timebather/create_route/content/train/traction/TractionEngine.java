package cn.timebather.create_route.content.train.traction;


import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.nbt.CompoundTag;

public class TractionEngine {
    static double FRICTION_COEFFICIENT = 0.001;
    static double WIND_FRICTION_COEFFICIENT = 0.03;

    static double TRAIN_WEIGHT = 30 * 1000;// 列车质量: 30 t

    double energy = 0; // 系统内总能量(单位:kJ = kW * s)

    double power = 0; // 牵引系统总功率(单位:kJ/s=kW)

    double constant_break_power = 0; // 恒功率制动系统总功率(电制动)
    double max_constant_break_power = 1000; // 恒功率制动系统总功率(电制动)
    double friction_break_coefficient = 0; // 恒比例制动系统总比例(空气制动)

    double max_friction_break_coefficient = 0.1; // 最大恒比例制动

    boolean forward = true;

    Train train;

    public TractionEngine(Train train){
        this.train = train;
    }

    double getConstantFrictionForce(){
        return WIND_FRICTION_COEFFICIENT * (train.speed * 20) * 1000;
    }

    double getActualSpeed(){
        return Math.sqrt(energy * 2 / TRAIN_WEIGHT);
    }

    public void tick(){
        if(this.power > 0)
            this.energy += power * 0.05d;

        if(this.energy > 0 && this.constant_break_power>0)
            this.energy -= this.constant_break_power * 0.05d; // 电制动

        if(this.energy >0 && this.friction_break_coefficient>0)
            this.energy = this.energy * (1 - friction_break_coefficient) * 0.05d;

        this.energy -= getConstantFrictionForce();

        this.energy = Math.max(0,this.energy);

        this.tickSpeed();
    }

    public void tickSpeed(){
        this.train.speed = getActualSpeed();
    }

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        tag.putDouble("Energy",energy);
        tag.putDouble("Power",power);
        tag.putDouble("ConstantBreakPower",constant_break_power);
        tag.putDouble("FrictionBreakCoefficient",friction_break_coefficient);
        tag.putBoolean("Forward",forward);
        return tag;
    }

    public void read(CompoundTag tag){
        this.energy = tag.getDouble("Energy");
        this.power = tag.getDouble("Power");
        this.constant_break_power = tag.getDouble("ConstantBreakPower");
        this.friction_break_coefficient = tag.getDouble("FrictionBreakCoefficient");
        this.forward = tag.getBoolean("Forward");
    }

    public void setPower(int power){
        if(power >= 0){
            this.power = power;
            this.constant_break_power = 0;
        }else{
            this.power = 0;
            this.constant_break_power = -power;
        }
    }

    public void setBreak(float v) {
        this.max_friction_break_coefficient = 0.001;
        this.friction_break_coefficient = v * max_friction_break_coefficient;
    }
}
