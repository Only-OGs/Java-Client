package Road;

import MathHelpers.Util;
import Rendering.ColorThemes;
import Screens.GameScreen;
import com.badlogic.gdx.graphics.Color;


public class RoadBuilder {
    /**
     * erstellt die Strecke
     * @param l die Anzahl der Segmente der Strecke
     * @param segmentLenght die Länge eines Einzelnen Segments
     * @return
     */
    public static Segment[] resetRoad(int l,int segmentLenght){
        Segment[] segments= new Segment[l];
        segments[0]=buildStart(segmentLenght);
        int index=1;
        index += addRoad(segments,segmentLenght,index,50,Curve.EASYLEFT,-40);
        index += addRoad(segments,segmentLenght,index,50,Curve.NONE,60);
        index += addRoad(segments,segmentLenght,index,50,Curve.MEDIUMLEFT,-20);

        for(int i=index;i<l-1;i++){
            addSegment(segments,segmentLenght,i,0,0);
        }
        segments[l-1]=buildfinsih(segmentLenght,l-1);
        return segments;
    }

    /**
     * Gibt die Farbpalette des Segments an //alle drei Segmente wird gewechselt
     */
    private static Color[] getColorTheme(int i) {
        if (Math.floor((i%6)/3) == 0) {
            return ColorThemes.getLight();
        } else {
            return ColorThemes.getDark();
        }
    }

    /**
     * erstellt das erste Segment der Strecke
     * @param Segmentlenght
     * @return
     */
    private static Segment buildStart(int Segmentlenght){
        P p1 = new P(new World(0*Segmentlenght,0),new Cam(),new Screen());
        P p2 = new P(new World((1)*Segmentlenght,0),new Cam(),new Screen());

        Segment s = new Segment(0,p1,p2,ColorThemes.getStart(),0);
        return s;
    }

    /**
     * erstellt das letzte Segment der Strecke/Ziel
     * @param Segmentlenght
     * @param index der Index eines Segments. Wird benötigt für den Index eines Segments selbst, als auch für die Position im Array
     * @return
     */
    private static Segment buildfinsih(int Segmentlenght,int index){
        P p1 = new P(new World(index*Segmentlenght,0),new Cam(),new Screen());
        P p2 = new P(new World((index+1)*Segmentlenght,0),new Cam(),new Screen());

        Segment s = new Segment(index,p1,p2,ColorThemes.getFinish(),0);
        return s;
    }

    /**
     * Fügt ein einzenlnes Segment hinzu
     * @param road Das Array der Straßen Segmente
     * @param segmentLenght
     * @param index
     * @param curve Gibt die Stärke der Kurve des einzelnen Segments an
     */
    private static void addSegment(Segment[] road,int segmentLenght, int index,float curve,int y){
        P p1 = new P(new World(index*segmentLenght,lastY(road,index)),new Cam(),new Screen());
        P p2 = new P(new World((index+1)*segmentLenght,y),new Cam(),new Screen());

        Segment s = new Segment(index,p1,p2,getColorTheme(index), curve);
        road[index]=s;
    }

    /**
     * Fügt ein Straßenabschnitt hinzu welcher eine Kurve oder Gerade Sein kann
     * @param road
     * @param segmentLenght
     * @param index
     * @param roadSegmentLenght
     * @param curve hier gibt dies die Stärke der Kutve am stärksten Punkt an
     * @return
     */
    private static int addRoad(Segment[] road,int segmentLenght,int index,int roadSegmentLenght, Curve curve,int y){
        double c = 0;
        int startY = lastY(road,index);
        int endY = startY + y*segmentLenght;
        for(int i=index;i<index+roadSegmentLenght;i++,c++){
            addSegment(road,segmentLenght,i, Util.easeIn(0,curve.getValue(),c/roadSegmentLenght),(int)Util.easeInOut(startY,endY,c/(3*roadSegmentLenght)));
        }
        c=0;
        index+=roadSegmentLenght;
        for(int i=index;i<index+roadSegmentLenght;i++,c++){
            addSegment(road,segmentLenght,i,curve.getValue(),(int) Util.easeInOut(startY,endY,(roadSegmentLenght+c)/(3*roadSegmentLenght)));
        }
        c=0;
        index+=roadSegmentLenght;
        for(int i=index;i<index+roadSegmentLenght;i++,c++){
            addSegment(road,segmentLenght,i, Util.easeInOut(0,curve.getValue(),c/roadSegmentLenght),(int) Util.easeInOut(startY,endY,(roadSegmentLenght*2+c)/(3*roadSegmentLenght)));
        }
        return 3*roadSegmentLenght;
    }
    private static int lastY(Segment[] road, int index){
        if(index==0){
            return 0;
        }else {
            return road[index-1].getP2().getWorld().getY();
        }
    }
}
