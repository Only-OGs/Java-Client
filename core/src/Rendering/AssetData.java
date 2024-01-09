package Rendering;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class AssetData {
    public static Texture[] txtCars = { new Texture("sprites/car01.png"),
                                        new Texture("sprites/car02.png"),
                                        new Texture("sprites/car03.png"),
                                        new Texture("sprites/car04.png"),
                                         new Texture("sprites/truck.png"),
                                        new Texture("sprites/semi.png")
    };
    public static Texture[] txtSprites = {new Texture("sprites/billboard01.png"),
            new Texture("sprites/billboard02.png"),
            new Texture("sprites/billboard03.png"),
            new Texture("sprites/billboard04.png"),
            new Texture("sprites/billboard05.png"),
            new Texture("sprites/billboard06.png"),
            new Texture("sprites/billboard07.png"),
            new Texture("sprites/billboard08.png"),
            new Texture("sprites/billboard09.png"),
            new Texture("sprites/boulder1.png"),
            new Texture("sprites/boulder2.png"),
            new Texture("sprites/boulder3.png"),
            new Texture("sprites/bush1.png"),
            new Texture("sprites/bush2.png"),
            new Texture("sprites/cactus.png"),
            new Texture("sprites/dead_tree1.png"),
            new Texture("sprites/dead_tree2.png"),
            new Texture("sprites/house1.png"),
            new Texture("sprites/house2.png"),
            new Texture("sprites/house3.png"),
            new Texture("sprites/palm_tree.png"),
            new Texture("sprites/stump.png"),
            new Texture("sprites/tree1.png"),
            new Texture("sprites/tree2.png")
    };
    public static Texture[] txtBackground = {new Texture("background/sky.png"),
            new Texture("background/hills.png"),
            new Texture("background/trees.png")
    };
    public static Texture[] txtPlayer = {new Texture("sprites/player_straight.png"),
            new Texture("sprites/player_uphill_straight.png"),
            new Texture("sprites/player_left.png"),
            new Texture("sprites/player_uphill_left.png"),
            new Texture("sprites/player_right.png"),
            new Texture("sprites/player_uphill_right.png")
    };
    public static Color[] getLight(){
        Color rumble = Color.valueOf("555555");
        Color grass = Color.valueOf("10aa10");
        Color road = Color.valueOf("6b6b6b");
        Color lane = Color.valueOf("cccccc");
        Color[] c = new Color []{road,rumble,grass,lane};
        return c;
    }
    public static Color[] getDark(){
        Color rumble = Color.valueOf("BBBBBB");
        Color grass = Color.valueOf("009A00");
        Color road = Color.valueOf("696969");
        Color lane = Color.valueOf("696969");
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
    public static Texture getCarSprite(int number){
       if(number > txtCars.length||number < 0){
           return txtCars[0];
       }else{
           return txtCars[number];
       }
    }
    public static Texture getSprite(int number){
        if(number > txtSprites.length||number < 0){
            return txtSprites[0];
        }else{
            return txtSprites[number];
        }
    }
    public static Texture getBackground(int number){
        if(number > txtBackground.length||number < 0){
            return txtBackground[0];
        }else{
            return txtBackground[number];
        }
    }
    public static Texture getplayer(int number){
        if(number > txtPlayer.length||number < 0){
            return txtPlayer[0];
        }else{
            return txtPlayer[number];
        }
    }
}
