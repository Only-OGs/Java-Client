package Rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpritesRenderer {
    public static void render(SpriteBatch batch, float resolution, int roadwidth, Texture t,float scale, float destX,float destY,float offsetX,float offsetY,float clipY){
        float spriteScale = 0.3f*(1f/80f);
        int width = Gdx.graphics.getWidth();

        int destW = (int) ((t.getWidth()*scale*width/2)*(spriteScale*roadwidth));
        int destH = (int) ((t.getHeight()*scale*width/2)*(spriteScale*roadwidth));

        destX = destX +(destW*(offsetX));
        destY = destY +(destH*(offsetY));


        float clipH = 0;
        if(clipY!=0){
            clipH = Math.max(0,destY+destH-clipY);
        }
        if(clipH < destH){
            //batch.draw(t,0,0,t.getWidth(),t.getHeight()-(t.getHeight()*clipH/destH),destX,libgdxDestY,destW,destH-clipH);
            //batch.draw(t,destX,destY, (float) destW,destH-clipH);
            batch.draw(t,destX,destY,destW,-(destH-clipH),0,0,t.getWidth(), (int)(t.getHeight()-(t.getHeight()*clipH/destH)),false,true);
        }
    }
}
