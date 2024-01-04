package Road;

import com.badlogic.gdx.graphics.Texture;

public class CustomSprite {
    public static Texture preCar1 = new Texture("sprites/car01.png");
    public static Texture preCar2 = new Texture("sprites/car02.png");
    public static Texture preCar3 = new Texture("sprites/car03.png");
    public static Texture preCar4 = new Texture("sprites/car04.png");
    public static Texture preCar5 = new Texture("sprites/semi.png");
    public static Texture preCar6 = new Texture("sprites/truck.png");
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
}
