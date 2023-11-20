package Road;

import Rendering.ColorThemes;
import Screens.GameScreen;
import com.badlogic.gdx.graphics.Color;


public class RoadBuilder {
    public static Segment[] resetRoad(int l,int Segmentlenght){
        Segment[] segments= new Segment[l];
        for (int i=0;i<l;i++){
            P p1 = new P(new World(i*Segmentlenght),new Cam(),new Screen());
            P p2 = new P(new World((i+1)*Segmentlenght),new Cam(),new Screen());

            Segment s = new Segment(i,p1,p2,getColorTheme(i));
            segments[i]=s;
        }
        return segments;
    }

    //Gibt die Farbpalette des Segments an
    private static Color[] getColorTheme(int i) {
        if (Math.floor((i%6)/3) == 0) {
            return ColorThemes.getLight();
        } else {
            return ColorThemes.getDark();
        }
    }
    private static Segment buildStart(int Segmentlenght){
        P p1 = new P(new World(0*Segmentlenght),new Cam(),new Screen());
        P p2 = new P(new World((1)*Segmentlenght),new Cam(),new Screen());

        Segment s = new Segment(0,p1,p2,ColorThemes.getStart());
        return s;
    }
    private static Segment buildfinsih(int Segmentlenght,int index){
        P p1 = new P(new World(index*Segmentlenght),new Cam(),new Screen());
        P p2 = new P(new World((index+1)*Segmentlenght),new Cam(),new Screen());

        Segment s = new Segment(0,p1,p2,ColorThemes.getFinish());
        return s;
    }
}
