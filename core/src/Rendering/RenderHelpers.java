package Rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RenderHelpers {
    public static float rumbleWidth(float RoadWidth,int lanes){
        return RoadWidth/Math.max(6,2*lanes);
    }
    public static float laneMarkerWidth(float RoadWidth,int lanes){
        return RoadWidth/Math.max(32,8*lanes);
    }
    public static void renderPolygon(ShapeRenderer r, float[]cords, Color c){
        r.begin(ShapeRenderer.ShapeType.Filled);
        r.setColor(c);
        r.triangle(cords[0],cords[1],cords[2],cords[3],cords[4],cords[5]);
        r.triangle(cords[0],cords[1],cords[4],cords[5],cords[6],cords[7]);
        r.end();
    }
}
