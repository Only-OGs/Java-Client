package Road;

import Screens.GameScreen;

public class Car {

    private String id;
    private CustomSprite customSprite;  // position und Aussehen des einzelnen Autos
    private Segment segment;  // Wird später berechnet


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
    public void place(){
        if(customSprite!=null){
            this.segment = findSegment(customSprite.getZ(),GameScreen.getSegments(),GameScreen.getSegmentLenght());
            this.segment.addCar(this);
        }
    }
    public void remove(){
        if(this.segment!= null){
            this.segment.removeCar(this);
        }
    }

    private Segment findSegment(double p,Segment[] segments,int segmentLenght) {
        return segments[(int) (Math.floor(p / segmentLenght) % segments.length)];
    }
    public void setZ(double z){
        customSprite.setZ(z);
        segment.removeCar(this);
        place();
    }
}
