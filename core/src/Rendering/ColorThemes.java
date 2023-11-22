package Rendering;


import com.badlogic.gdx.graphics.Color;

public class ColorThemes {
    public static Color[] getLight(){
        /*Color rumble = Color.valueOf("555555");
        Color grass = Color.valueOf("10aa10");
        Color road = Color.valueOf("6b6b6b");
        Color lane = Color.valueOf("cccccc");*/
        Color rumble = Color.valueOf("FF06C1");
        Color grass = Color.valueOf("00ffea");
        Color road = Color.valueOf("14152c");
        Color lane = Color.valueOf("cccccc");
        Color[] c = new Color []{road,rumble,grass,lane};
        return c;
    }
    public static Color[] getDark(){
        Color rumble = Color.valueOf("ba2e97");
        Color grass = Color.valueOf("37d6c9");
        Color road = Color.valueOf("14152c");
        Color lane = Color.valueOf("14152c");
        /*Color rumble = Color.valueOf("BBBBBB");
        Color grass = Color.valueOf("009A00");
        Color road = Color.valueOf("696969");
        Color lane = Color.valueOf("696969");*/
        Color[] c = new Color []{road,rumble,grass,lane};
        return c;
    }
    public static Color[] getStart(){
        Color rumble = Color.WHITE;
        Color grass = Color.WHITE;
        Color road = Color.WHITE;
        Color lane = Color.WHITE;
        Color[] c = new Color []{road,rumble,grass,lane};
        return c;
    }
    public static Color[] getFinish(){
        Color rumble = Color.BLACK;
        Color grass = Color.BLACK;
        Color road = Color.BLACK;
        Color lane = Color.BLACK;
        Color[] c = new Color []{road,rumble,grass,lane};
        return c;
    }
}
