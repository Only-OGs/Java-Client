package Rendering;

public class RenderHelpers {
    public static float rumbleWidth(float RoadWidth,int lanes){
        return RoadWidth/Math.max(6,2*lanes);
    }
    public static float laneMarkerWidth(float RoadWidth,int lanes){
        return RoadWidth/Math.max(32,8*lanes);
    }
}
