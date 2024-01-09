package Road;

import Rendering.AssetData;
import com.badlogic.gdx.graphics.Texture;

/**
 * Diese Klasse verbindet ein/e Sprite/Textur mit Koordinaten in der Welt. Simuliert also ein Objekt in der Welt
 */
public class CustomSprite {
    Texture t;
    float offset;
    double z;

    /**
     * Beim Erstellen eines Spriteobjekts wird diesen eine Textur zugewiesen welche in AssetData gespeichert ist und z und x Koordinaten
     * @param offset
     * @param z
     */
    public CustomSprite(float offset, double z) {
        this.t = AssetData.getCarSprite(0);
        this.offset = offset;
        this.z = z;
    }

    public CustomSprite(String texturepath,float offset,double z){
        switch (texturepath){
            case "car01.png" -> this.t = AssetData.getCarSprite(0);
            case "car02.png" -> this.t = AssetData.getCarSprite(1);
            case "car03.png" -> this.t = AssetData.getCarSprite(2);
            case "car04.png" -> this.t = AssetData.getCarSprite(3);
            case "truck.png" -> this.t = AssetData.getCarSprite(4);
            case "semi.png" -> this.t  = AssetData.getCarSprite(5);
            default -> this.t = AssetData.getSprite(Integer.parseInt(texturepath));
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
