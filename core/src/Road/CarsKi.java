package Road;


import MathHelpers.Util;
import Screens.GameScreen;
import com.badlogic.gdx.Gdx;


public class CarsKi {
    public static void kiMoveBot(Car c){
        int fps = Gdx.graphics.getFramesPerSecond();
        c.setOffset(c.getCs().getOffset()+updateOffset(c));
        c.setZ(Util.increase((int) c.getCs().getZ(), (int) (1f/fps*c.getSpeed()), GameScreen.getSegmentLenght()*GameScreen.getSegments().length));
        c.setPercent((float) Util.percentRemaining((float) c.getCs().getZ(),GameScreen.getSegmentLenght()));
    }
    //todo
    static float updateOffset(Car c){
        return 0;
    }

}