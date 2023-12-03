package Road;

import MathHelpers.Util;
import Rendering.ColorThemes;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;


public class RoadBuilder {

    private static Texture t = new Texture("sprites/tree1.png");
    private static Texture tCar = new Texture("sprites/car01.png");
    /**
     * erstellt die Strecke
     * @param l die Anzahl der Segmente der Strecke
     * @param segmentLenght die Länge eines Einzelnen Segments
     * @return
     */
    public static Segment[] resetRoad(int l,int segmentLenght){
        CustomSprite [] arr= createSpriteArr(l);
        ArrayList<Segment> segments= new ArrayList<>();
        segments.add(buildStart(segmentLenght));
        int index=1;
        index += addRoad(segments,segmentLenght,index,50,Curve.HARDLeft.getValue(),60);
        index += addRoad(segments,segmentLenght,index,50,Curve.EASYRIGHT.getValue(),-20);
        index += addRoad(segments,segmentLenght,index,50,Curve.MEDIUMRIGHT.getValue(),-40);

        for(int i=index;i<l-1;i++){
            addSegment(segments,segmentLenght,i,0,0);
        }
        segments.add(buildfinsih(segmentLenght,l-1));
        Segment[] s = segments.toArray(Segment[]::new);
        addSprites(s,arr,200);
        return (s);
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
    private static void addSegment(ArrayList<Segment> road,int segmentLenght, int index,float curve,int y){
        P p1 = new P(new World(index*segmentLenght,lastY(road,index)),new Cam(),new Screen());
        P p2 = new P(new World((index+1)*segmentLenght,y),new Cam(),new Screen());

        Segment s = new Segment(index,p1,p2,getColorTheme(index), curve);
        road.add(s);
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
    private static int addRoad(ArrayList<Segment> road,int segmentLenght,int index,int roadSegmentLenght, float curve,int y){
        double c = 0;
        int startY = lastY(road,index);
        int endY = startY + y*segmentLenght;
        for(int i=index;i<index+roadSegmentLenght;i++,c++){
            addSegment(road,segmentLenght,i, Util.easeIn(0,curve,c/roadSegmentLenght),(int)Util.easeInOut(startY,endY,c/(3*roadSegmentLenght)));
        }
        c=0;
        index+=roadSegmentLenght;
        for(int i=index;i<index+roadSegmentLenght;i++,c++){
            addSegment(road,segmentLenght,i,curve,(int) Util.easeInOut(startY,endY,(roadSegmentLenght+c)/(3*roadSegmentLenght)));
        }
        c=0;
        index+=roadSegmentLenght;
        for(int i=index;i<index+roadSegmentLenght;i++,c++){
            addSegment(road,segmentLenght,i, Util.easeInOut(0,curve,c/roadSegmentLenght),(int) Util.easeInOut(startY,endY,(roadSegmentLenght*2+c)/(3*roadSegmentLenght)));
        }
        return 3*roadSegmentLenght;
    }
    private static int lastY(ArrayList<Segment> road, int index){
        if(index==0){
            return 0;
        }else {
            return road.get(index-1).getP2().getWorld().getY();
        }
    }
    private static void addSprites(Segment[] segments,CustomSprite [] cs,int segmentlenght){
        for (CustomSprite c: cs) {
            Segment s = findSegment(segments,c.getZ(),segmentlenght);
            s.addSprite(c);
        }
    }
    private static Segment findSegment(Segment[] s, double p,int segmentLenght) {
        return s[(int) (Math.floor(p / segmentLenght) % s.length)];
    }
    private static CustomSprite[] createSpriteArr(int l){
        CustomSprite[] cs= new CustomSprite[l/4];
        for(int i=0;i<l/4;i++){
            CustomSprite s = new CustomSprite(t,i%4==0?-1:1,i*4*1000);
            cs[i]=s;
        }
        return cs;
    }
    public static Car[] createCarArr(int l){
        CustomSprite[] cs= new CustomSprite[l/10];
        for(int i=0;i<l/10;i++){
            CustomSprite s = new CustomSprite(tCar,i%4==0?-0.5f:0.5f,i*10*500);
            cs[i]=s;
        }
        Car[] cars = new Car[l/10];
        for(int i = 0;i<l/10;i++){
            Car temp = new Car();
            temp.setCs(cs[i]);
            cars[i]= temp;
        }
        return  cars;
    }
}