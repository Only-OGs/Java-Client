package Road;

import com.badlogic.gdx.graphics.Texture;

public class CustomSprite {
    Texture t;
    float offset;
    double z;

    public CustomSprite(Texture t, float offset, double z) {
        this.t = t;
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
}
