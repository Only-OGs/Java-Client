package Rendering;

import Road.Helper.Segment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RenderSegment {
    public static void render(ShapeRenderer shape, int width, int lanes, float x1, float y1, float w1, float x2, float y2, float w2, double fog, Color[] color, Segment segment){
        float r1 = RenderHelpers.rumbleWidth(w1,lanes);
        float r2 = RenderHelpers.rumbleWidth(w2,lanes);
        float l1 = RenderHelpers.laneMarkerWidth(w1,lanes);
        float l2 = RenderHelpers.laneMarkerWidth(w2,lanes);

        float libgdxY1 = Gdx.graphics.getHeight()-y1;
        float libgdxY2 = Gdx.graphics.getHeight()-y2;

        //Rendert einen Grünen Streifen auf höhe des Segments
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(color[2]);
        shape.rect(0,libgdxY2,width,libgdxY1-libgdxY2);
        shape.end();


        //Rendert die Straße und den Rand
        float[]cords1 = {x1-w1-r1,libgdxY1,x1-w1,libgdxY1,x2-w2,libgdxY2,x2-w2-r2,libgdxY2};

        float[]cords2 = new float[]{x1+w1+r1,libgdxY1,x1+w1,libgdxY1,x2+w2,libgdxY2,x2+w2+r2,libgdxY2};

        float[] cords3 = new float[]{x1-w1,libgdxY1,x1+w1,libgdxY1,x2+w2,libgdxY2,x2-w2,libgdxY2};


        //Rendert die Straße
        RenderHelpers.renderPolygon(shape,cords1,color[1]);
        RenderHelpers.renderPolygon(shape,cords2,color[1]);
        RenderHelpers.renderPolygon(shape,cords3,color[0]);
        if(true){
            float lanew1 = w1*2/lanes;
            float lanew2 = w2*2/lanes;
            float lanex1 = x1-w1+lanew1;
            float lanex2 = x2-w2+lanew2;
            for(int lane=1;lane<lanes;lane++){
                RenderHelpers.renderPolygon(shape,new float[]{lanex1-l1/2,libgdxY1,lanex1+l1/2,libgdxY1,lanex2+l2/2,libgdxY2,lanex2-l2/2,libgdxY2},color[3]);
                lanex1+=lanew1;
                lanex2+=lanew2;
            }
        }
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color( 0f/255f, 81f/255f, 8f/255f, (float) fog));
        shape.rect(0, libgdxY1, Gdx.graphics.getWidth(),libgdxY2-libgdxY1);
        shape.end();
    }
}
