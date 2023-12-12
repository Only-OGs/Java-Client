package Road;

import com.badlogic.gdx.graphics.Texture;

public class CustomSprite {
    public Texture preCar = new Texture("sprites/car02.png");
    Texture t;
    float offset;
    double z;

    public CustomSprite(Texture t, float offset, double z) {
        this.t = t;
        this.offset = offset;
        this.z = z;
    }
    public CustomSprite(float offset, double z) {
        this.t = preCar;
        this.offset = offset;
        this.z = z;
    }
    public CustomSprite(String texturepath,float offset,double z){
        this.t = new Texture("sprites/"+texturepath);
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
