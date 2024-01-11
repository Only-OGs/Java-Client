package Road;

import Road.Helper.Segment;
import Screens.GameScreen;

/**
 * Diese Klasse stellt Autos dar welche eine Geschwindigkeit,id, CustomSprite (Zum Speichern von aussehen und momentaner position),
 * und das Segment in den sich das Auto befindet.
 */
public class Car {

    private String id;
    private CustomSprite customSprite;  // position und Aussehen des einzelnen Autos
    private Segment segment;  // Wird später berechnet
    private float percent=0;
    private int speed;


    public Car() {}

    public Car(String id, CustomSprite customSprite) {
        this.id = id;
        this.customSprite = customSprite;
    }

    public CustomSprite getCs() {
        return customSprite;
    }

    public Segment getS() {
        return segment;
    }

    public void setS(Segment s) {
        this.segment = s;
    }

    public void setCs(CustomSprite cs) {this.customSprite = cs;}

    /**
     * fügt das Auto zu dem passenden Segment hinzu so, dass dieses weiß das, dass Auto gerade dort steht
     */
    public void place(){
        if(customSprite!=null){
            this.segment = GameScreen.findSegment(customSprite.getZ());
            this.segment.addCar(this);
        }
    }

    /**
     * wenn ein Auto sich vorwärts bewegt muss es sich bei dem alten Segment sozusagen abmelden
     */
    public void remove(){
        if(this.segment!= null){
            this.segment.removeCar(this);
        }
    }

    public void setZ(double z){
        remove();
        customSprite.setZ(z);
        segment.removeCar(this);
        place();
    }
    public void setOffset(float offset){
        customSprite.setOffset(offset);
    }
    public String getID(){
        return id;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
