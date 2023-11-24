package Rendering;

import MathHelpers.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SunShade {
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
    public static void sun(ShapeRenderer r,float sunOffset){
        r.begin(ShapeRenderer.ShapeType.Filled);
        r.setColor(Color.WHITE);
        r.circle(Gdx.graphics.getWidth()/2+sunOffset*3,Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/3);
        r.end();
    }
}
