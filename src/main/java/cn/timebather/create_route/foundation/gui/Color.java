package cn.timebather.create_route.foundation.gui;

public class Color {
    protected final int colorCode;

    public static final Color RED = Color.hex(0xFFFF0000);
    public static final Color GREEN = Color.hex(0xFF00FF00);
    public static final Color BLUE = Color.hex(0xFF0000FF);
    public static final Color YELLOW = Color.hex(0xFFFFFF00);
    public static final Color PURPLE = Color.hex(0xFFFF00FF);
    public static final Color WHITE = Color.hex(0xFFFFFFFF);
    public static final Color BLACK = Color.hex(0xFF000000);
    public static final Color TRANSPARENT = Color.hex(0x00000000);
    protected Color(int colorCode){
        this.colorCode = colorCode;
    }

    public static Color of(int r,int g,int b){
        return of(r,g,b,0);
    }

    public static Color of(int r,int g,int b,int alpha){
        return new Color(r << 24 + g << 16 + b << 8 + alpha);
    }

    public static Color hex(int hexCode){
        return new Color(hexCode);
    }

    public int getRed(){
        return (colorCode >> 24) & 0xFF;
    }

    public int getGreen(){
        return (colorCode >> 16) & 0xFF;
    }

    public int getBlue(){
        return (colorCode >> 8) & 0xFF;
    }

    public int getAlpha(){
        return (colorCode) & 0xFF;
    }

    public static float PERCENTAGE = 1f/255f;

    public float getRedPercentage(){
        return getRed() * PERCENTAGE;
    }
    public float getGreenPercentage(){
        return getGreen() * PERCENTAGE;
    }
    public float getBluePercentage(){
        return getBlue() * PERCENTAGE;
    }
    public float getAlphaPercentage(){
        return getAlpha() * PERCENTAGE;
    }
}
