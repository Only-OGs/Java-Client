package Rendering;

import MathHelpers.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SunShade {
    static Texture t =  new Texture("Background/Moon.png");
    public static void sunShade(ShapeRenderer r, float sunOffset, int maxy, float renderedCounter){
        r.begin(ShapeRenderer.ShapeType.Filled);
        float centerX=Gdx.graphics.getWidth()/2+sunOffset*3;
        float centerY=Gdx.graphics.getHeight()/2;
        float width=Gdx.graphics.getWidth()/3;
        float offset = maxy-centerY;
        float offsetpercent = 1+offset/Gdx.graphics.getHeight();
        float rectW = (float) (Math.sqrt(width*width-offset*offset));
        float steps = (Gdx.graphics.getHeight()-maxy)/renderedCounter;
        for(int i=1; i <= renderedCounter; i++){
            r.setColor(new Color(Color.WHITE.r,Color.WHITE.g,Color.WHITE.b, (float) (1-Util.exponentialFog(i/renderedCounter*offsetpercent*0.2f,4))));
            r.rect(centerX-rectW,i*steps,rectW*2,-steps);
        }
        r.end();
    }
    public static void sun(ShapeRenderer r, SpriteBatch b,float sunOffset,int clip){
        /*r.begin(ShapeRenderer.ShapeType.Filled);
        r.setColor(Color.WHITE);
        r.circle(Gdx.graphics.getWidth()/2+sunOffset*3,Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/3);
        r.end();*/
        int radius = Gdx.graphics.getWidth()/5;
        int width =  Gdx.graphics.getWidth();
        int hight =  Gdx.graphics.getHeight();
        int centerH = (int) (hight/1.3);
        int destX = (int) ((width/2)-radius+sunOffset*3);
        int destY = centerH-radius;            //unten
        if(destY<hight-clip){    // unten ggf = clip
            destY=hight-clip;
        }
        int destW = radius*2;                    //breite
        int destH = centerH+radius-destY;      // höchster punkt - unten
        float percent = 1f-(destH/(radius*2f)); // tatsächliche höhe durch volle höhe

        if(hight-clip<centerH+radius){
            b.begin();
            b.draw(t,destX,destY+1,destW,destH,0,0,t.getWidth(), (int) (t.getHeight()-t.getHeight()*percent),false,false);
            b.end();
        }
    }
}
