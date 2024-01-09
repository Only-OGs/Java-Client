package Road;

import Helpers.Util;
import Rendering.AssetData;
import Road.Helper.*;
import Screens.GameScreen;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;


public class RoadBuilder {

    /**
     * Enthält verschiedene Funktionen um eine Stracke zu Laden und mit sprites zu befüllen
     * @param l die Anzahl der Segmente der Strecke
     * @param segmentLenght die Länge eines einzelnen Segments
     * @return
     */
    public static Segment[] resetRoad(int l, int segmentLenght){
        ArrayList<Segment> segments= new ArrayList<>();
        segments.add(buildStart(segmentLenght));
        int index=1;
        index += addRoad(segments,segmentLenght,index,50, Curve.HARDLeft.getValue(),60);
        index += addRoad(segments,segmentLenght,index,50,Curve.EASYRIGHT.getValue(),-20);
        index += addRoad(segments,segmentLenght,index,50,Curve.MEDIUMRIGHT.getValue(),-40);

        for(int i=index;i<l-1;i++){
            addSegment(segments,segmentLenght,i,0,0);
        }
        segments.add(buildfinsih(segmentLenght,l-1));
        Segment[] s = segments.toArray(Segment[]::new);
        return (s);
    }
    public static void fillSprites(){
        CustomSprite [] arr= createSpriteArr(GameScreen.getSegments().length);
        addSprites(GameScreen.getSegments(),arr);
    }

    /**
     * Erstellt eine Strecke mithilfe der gesendeten Daten die in RoadPart abgespeichert wurden
     * @param road
     * @return
     */
    public static Segment[] resetRoad(RoadPart[] road){
        int segmentLenght = GameScreen.getSegmentLength();
        ArrayList<Segment> segments= new ArrayList<>();
        segments.add(buildStart(segmentLenght));
        int index=1;
        for(RoadPart rp:road){
            index+=addRoad(segments,segmentLenght,index,rp.getLenght(),rp.getCurve(),rp.getHill());
        }segments.remove(segments.size()-1);
        segments.add(buildfinsih(segmentLenght,index-1));
        Segment[] s = segments.toArray(Segment[]::new);
        return (s);
    }

    /**
     * Gibt die Farbpalette des Segments an //alle drei Segmente wird gewechselt
     */
    private static Color[] getColorTheme(int i) {
        if (Math.floor((i%6)/3) == 0) {
            return AssetData.getLight();
        } else {
            return AssetData.getDark();
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

        Segment s = new Segment(0,p1,p2, AssetData.getStart(),0);
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

        Segment s = new Segment(index,p1,p2, AssetData.getFinish(),0);
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
     * Fügt ein Straßenabschnitt hinzu, welcher eine Kurve oder Gerade Sein kann
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

    /**
     * fügt der Strecke die CustomSprite Objekte hinzu
     * @param segments
     * @param cs
     */
    public static void addSprites(Segment[] segments,CustomSprite [] cs){
        for (CustomSprite c: cs) {
            Segment s = GameScreen.findSegment(c.getZ());
            s.addSprite(c);
        }
    }

    /**
     * erstellt ein Array von Sprites für den Singleplayer
     * @param l
     * @return
     */
    private static CustomSprite[] createSpriteArr(int l){
        CustomSprite[] cs= new CustomSprite[l/4];
        for(int i=0;i<l/4;i++){
            int number = (int) (Math.random()*3+21);
            CustomSprite s = new CustomSprite(String.valueOf(number),i%4==0?-1:1,i*4*400);
            cs[i]=s;
        }
        return cs;
    }

    /**
     * erst ein Array von Bot autos für den Singleplayer
     * @param l
     * @return
     */
    public static Car[] createCarArr(int l){
        CustomSprite[] cs= new CustomSprite[l/40];
        for(int i=0;i<l/40;i++){
            CustomSprite s = new CustomSprite(i%4==0?-0.5f:0.5f,i*40*500);
            cs[i]=s;
        }
        Car[] cars = new Car[l/40];
        for(int i = 0;i<l/40;i++){
            Car temp = new Car();
            temp.setCs(cs[i]);
            temp.setSpeed(Util.randomInt(100,150)*60);
            cars[i]= temp;
        }
        return  cars;
    }
}