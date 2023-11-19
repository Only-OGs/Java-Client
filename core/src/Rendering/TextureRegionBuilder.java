package Rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionBuilder {
    static TextureRegion textureRegionDarkRoad;
    static TextureRegion textureRegionDarkRumble;
    static TextureRegion textureRegionLane;
    static TextureRegion textureRegionLightRoad;
    static TextureRegion textureRegionLightRumble;


    public TextureRegionBuilder() {
        //Dark Road
        Pixmap pixDarkRoad = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixDarkRoad.setColor(ColorThemes.getDark()[0]);
        pixDarkRoad.fill();
        //Dark Rumble
        Pixmap pixDarkRumble = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixDarkRumble.setColor(ColorThemes.getDark()[1]);
        pixDarkRumble.fill();
        //Lane
        Pixmap pixLane = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixLane.setColor(ColorThemes.getLight()[3]);
        pixLane.fill();
        //Light Road
        Pixmap pixWhiteRoad = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixWhiteRoad.setColor(ColorThemes.getLight()[0]);
        pixWhiteRoad.fill();
        //Light Rumble
        Pixmap pixWhiteRumble = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixWhiteRumble.setColor(ColorThemes.getLight()[1]);
        pixWhiteRumble.fill();

        //Texture Dark Road
        Texture texture = new Texture(pixDarkRoad);
        textureRegionDarkRoad = new TextureRegion(texture);
        //Texture Dark Rumble
        texture = new Texture(pixDarkRumble);
        textureRegionDarkRumble = new TextureRegion(texture);
        //Texture Lane
        texture = new Texture(pixLane);
        textureRegionLane = new TextureRegion(texture);
        // Texture Light Road
        texture = new Texture(pixWhiteRoad);
        textureRegionLightRoad = new TextureRegion(texture);
        //Texture Light Rumble
        texture = new Texture(pixWhiteRumble);
        textureRegionLightRumble = new TextureRegion(texture);
    }
    public static TextureRegion getTexture(Color c){
        Color[] colorLight = ColorThemes.getLight();
        Color[] colorDark = ColorThemes.getDark();
        if(c==colorLight[0]){
            return textureRegionLightRoad;
        } else if (c.equals(colorLight[1])) {
            return textureRegionLightRumble;
        } else if (c.equals(colorLight[3])) {
            return textureRegionLane;
        } else if (c.equals(colorDark[0])) {
            return textureRegionDarkRoad;
        } else if (c.equals(colorDark[1])) {
            return textureRegionDarkRumble;
        }else{
            return textureRegionDarkRoad;
        }
    }
}
