package Rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpritesRenderer {
    public static void render(SpriteBatch batch, float resolution, int roadwidth, Texture t,double scale, float destX,float destY,float offsetX,float offsetY,float clipY){
        float spriteScale = 0.3f*(1f/80f);
        int width = Gdx.graphics.getWidth();

        float destW = (float) (t.getWidth()*scale*width/2)*(spriteScale*roadwidth);
        float destH = (float) (t.getHeight()*scale*width/2)*(spriteScale*roadwidth);

        destX = destX +(destW*(offsetX));
        destY = destY +(destH*(offsetY));


        float clipH = 0;
        if(clipY!=0){
            clipH = Math.max(0,destY+destH-clipY);
        }
        if(clipH < destH){
            batch.begin();
            batch.draw(t,destX,Gdx.graphics.getHeight()-destY,destW,-(destH-clipH),0,0,t.getWidth(), (int)(t.getHeight()-(t.getHeight()*clipH/destH)),false,true);
            batch.end();
        }
    }
}
