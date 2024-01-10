package Road;


import Helpers.Util;
import Road.Helper.Segment;
import Screens.GameScreen;
import com.badlogic.gdx.Gdx;


public class CarController {
    /**
     * Klasse zum Steuern der KI Autos im SinglePlayer
     * @param newCars
     */
    private static GameScreen gameScreen;
    public static void setup(GameScreen gs){
        gameScreen=gs;
    }
    public static void kiMoveBot(Car[] newCars){
        int fps = Gdx.graphics.getFramesPerSecond();
        for(Car c: newCars){
            c.setOffset(c.getCs().getOffset()+updateOffset(c));
            c.setZ(Util.increase((int) c.getCs().getZ(), (int) (1f/fps*c.getSpeed()), GameScreen.getSegmentLength()*GameScreen.getSegments().length));
            c.setPercent((float) Util.percentRemaining((float) c.getCs().getZ(),GameScreen.getSegmentLength()));
        }
    }

    /**
     * Berechnet wie Stark die Botautos ausweichen also ihren offset ändern sollen abhängig vom Spieler oder anderen Autos,
     * die vor ihnen sind. Sollte ein Auto die Straße verlassen haben, wird dieses wieder zurückgeführt.
     * @param c
     * @return
     */
    private static float updateOffset(Car c){
        float carW=c.getCs().getT().getWidth()*gameScreen.getSpriteScale();
        float dir;
        float playerX=gameScreen.getPlayerX();
        Segment temp;
        Segment[] segments = GameScreen.getSegments();
        Segment carSegment = GameScreen.findSegment(c.getCs().getZ());
        Segment playerSegment = GameScreen.findSegment(gameScreen.getCameraPosition());
        if(carSegment.getIndex()-playerSegment.getIndex()>gameScreen.getFullRenderer().getDrawDistance()){
            return 0;
        }
        for(int i=1; i<20;i++){
            temp = segments[(carSegment.getIndex()+i) % segments.length];
            if((temp==playerSegment)&&c.getSpeed()>gameScreen.getPlayerSpeed()){
                if(playerX>0.5f){
                    dir=-1f;
                } else if (playerX<-0.5f) {
                    dir=1f;
                }else{
                    dir=(c.getCs().getOffset()>playerX)? 1f:-1f;
                }
                return dir*1f/i*(c.getSpeed()-gameScreen.getPlayerSpeed())/gameScreen.getPlayerMaxSpeed();
            }
            if(temp.getCars()!=null){
                for(Car car: temp.getCars()){
                    float spriteW = (car.getCs().getT().getWidth()*gameScreen.getSpriteScale());
                    if((c.getSpeed()>car.getSpeed())&&Util.overlap(c.getCs().getOffset()+0.1f,carW,car.getCs().getOffset(),spriteW,1.2)){
                        if(car.getCs().getOffset()>0.5f){
                            dir=-1f;
                        } else if (car.getCs().getOffset()<-0.5f) {
                            dir=1f;
                        }else{
                            dir=(c.getCs().getOffset()>car.getCs().getOffset()?1f:-1f);
                        }
                        return dir*1f/i*(c.getSpeed()-gameScreen.getPlayerSpeed())/gameScreen.getPlayerMaxSpeed();
                    }
                }
            }
        }
        if(c.getCs().getOffset()<-0.9f)return 0.1f;
        else if (c.getCs().getOffset()>0.9f) return -0.1f;
        else return 0f;
    }
}