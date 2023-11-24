package Rendering;

import Road.Segment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CarRenderer {
    static Texture tS = new Texture("sprites/player_straight.png");
    static Texture tUpS = new Texture("sprites/player_uphill_straight.png");
    static Texture tL = new Texture("sprites/player_left.png");
    static Texture tUpL = new Texture("sprites/player_uphill_left.png");
    static Texture tR = new Texture("sprites/player_right.png");
    static Texture tUpR = new Texture("sprites/player_uphill_right.png");


    public static void renderPlayerCar(SpriteBatch batch, Segment curr){
        batch.begin();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getWidth();
        if(curr.getP1().getWorld().getY()<curr.getP2().getWorld().getY()){
            if(curr.getCurve()<0){
                batch.draw(tUpL,width/2-(width/4)/2,50,width/4,height/5);
            } else if (curr.getCurve()>0) {
                batch.draw(tUpR,width/2-(width/4)/2,50,width/4,height/5);
            }else {
                batch.draw(tUpS,width/2-(width/4)/2,50,width/4,height/5);
            }
        }else{
            if(curr.getCurve()<0){
                batch.draw(tL,width/2-(width/4)/2,50,width/4,height/5);
            } else if (curr.getCurve()>0) {
                batch.draw(tR,width/2-(width/4)/2,50,width/4,height/5);
            }else {
                batch.draw(tS,width/2-(width/4)/2,50,width/4,height/5);
            }
        }
        batch.end();
    }
}
