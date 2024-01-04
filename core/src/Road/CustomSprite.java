package Road;

import com.badlogic.gdx.graphics.Texture;

public class CustomSprite {
    public static Texture preCar1 = new Texture("sprites/car01.png");
    public static Texture preCar2 = new Texture("sprites/car02.png");
    public static Texture preCar3 = new Texture("sprites/car03.png");
    public static Texture preCar4 = new Texture("sprites/car04.png");
    public static Texture preCar5 = new Texture("sprites/semi.png");
    public static Texture preCar6 = new Texture("sprites/truck.png");
    public static Texture sprite0 = new Texture("sprites/billboard01.png");
    public static Texture sprite1 = new Texture("sprites/billboard02.png");
    public static Texture sprite2 = new Texture("sprites/billboard03.png");
    public static Texture sprite3 = new Texture("sprites/billboard04.png");
    public static Texture sprite4 = new Texture("sprites/billboard05.png");
    public static Texture sprite5 = new Texture("sprites/billboard06.png");
    public static Texture sprite6 = new Texture("sprites/billboard07.png");
    public static Texture sprite7 = new Texture("sprites/billboard08.png");
    public static Texture sprite8 = new Texture("sprites/billboard09.png");
    public static Texture sprite9 = new Texture("sprites/boulder1.png");
    public static Texture sprite10 = new Texture("sprites/boulder2.png");
    public static Texture sprite11 = new Texture("sprites/boulder3.png");
    public static Texture sprite12 = new Texture("sprites/bush1.png");
    public static Texture sprite13 = new Texture("sprites/bush2.png");
    public static Texture sprite14 = new Texture("sprites/cactus.png");
    public static Texture sprite15 = new Texture("sprites/dead_tree1.png");
    public static Texture sprite16 = new Texture("sprites/dead_tree2.png");
    public static Texture sprite17 = new Texture("sprites/house1.png");
    public static Texture sprite18 = new Texture("sprites/house2.png");
    public static Texture sprite19 = new Texture("sprites/house3.png");
    public static Texture sprite20 = new Texture("sprites/palm_tree.png");
    public static Texture sprite21 = new Texture("sprites/stump.png");
    public static Texture sprite22 = new Texture("sprites/tree1.png");
    public static Texture sprite23 = new Texture("sprites/tree2.png");

    Texture t;
    float offset;
    double z;

    public CustomSprite(Texture t, float offset, double z) {
        this.t = t;
        this.offset = offset;
        this.z = z;
    }
    public CustomSprite(float offset, double z) {
        this.t = preCar2;
        this.offset = offset;
        this.z = z;
    }
    public CustomSprite(String texturepath,float offset,double z){
        switch (texturepath){
            case "car01.png" -> this.t =preCar1;
            case "car02.png" -> this.t =preCar2;
            case "car03.png" -> this.t =preCar3;
            case "car04.png" -> this.t =preCar4;
            case "truck.png" -> this.t =preCar6;
            case "semi.png" -> this.t =preCar5;
            case "0" -> this.t =sprite0;
            case "1" -> this.t =sprite1;
            case "2" -> this.t =sprite2;
            case "3" -> this.t =sprite3;
            case "4" -> this.t =sprite4;
            case "5" -> this.t =sprite5;
            case "6" -> this.t =sprite6;
            case "7" -> this.t =sprite7;
            case "8" -> this.t =sprite8;
            case "9" -> this.t =sprite9;
            case "10" -> this.t =sprite10;
            case "11" -> this.t =sprite11;
            case "12" -> this.t =sprite12;
            case "13" -> this.t =sprite13;
            case "14" -> this.t =sprite14;
            case "15" -> this.t =sprite15;
            case "16" -> this.t =sprite16;
            case "17" -> this.t =sprite17;
            case "18" -> this.t =sprite18;
            case "19" -> this.t =sprite19;
            case "20" -> this.t =sprite20;
            case "21" -> this.t =sprite21;
            case "22" -> this.t =sprite22;
            case "23" -> this.t =sprite23;
            default -> this.t =preCar2;
        }
        this.offset = offset;
        this.z = z;
    }

    public Texture getT() {
        return t;
    }

    public float getOffset() {
        return offset;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {this.z = z;}

    public void setOffset(float offset) {
        this.offset=offset;
    }
}
