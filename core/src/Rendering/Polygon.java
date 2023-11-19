package Rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Polygon {
    static PolygonSprite poly;
    static Texture texture;
    static TextureRegion textureRegion;

    public static void renderPolygon(PolygonSpriteBatch polyBatch,float[]cords, Color c){
        /*Pixmap pixrumble = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixrumble.setColor(c);
        pixrumble.fill();

        texture = new Texture(pixrumble);
        textureRegion = new TextureRegion(texture);*/

        short[]shawty= new short[]{0,1,2,0,2,3};
        PolygonRegion polyReg = new PolygonRegion(TextureRegionBuilder.getTexture(c),cords,shawty);

        poly= new PolygonSprite(polyReg);

        poly.draw(polyBatch);
    }
}
