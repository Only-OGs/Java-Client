package Road;


import Helpers.Util;
import Screens.GameScreen;
import com.badlogic.gdx.Gdx;


public class CarController {
    public static void kiMoveBot(Car[] newCars){
        int fps = Gdx.graphics.getFramesPerSecond();
        for(Car c: newCars){
            c.setOffset(c.getCs().getOffset()+updateOffset(c));
            c.setZ(Util.increase((int) c.getCs().getZ(), (int) (1f/fps*c.getSpeed()), GameScreen.getSegmentLenght()*GameScreen.getSegments().length));
            c.setPercent((float) Util.percentRemaining((float) c.getCs().getZ(),GameScreen.getSegmentLenght()));
        }
    }
    //todo
    static float updateOffset(Car c){
        return 0;
    }

}