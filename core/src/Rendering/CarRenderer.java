package Rendering;

import MathHelpers.Util;
import Road.Segment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CarRenderer {
    public static Texture tS = new Texture("sprites/player_straight.png");
    static Texture tUpS = new Texture("sprites/player_uphill_straight.png");
    static Texture tL = new Texture("sprites/player_left.png");
    static Texture tUpL = new Texture("sprites/player_uphill_left.png");
    static Texture tR = new Texture("sprites/player_right.png");
    static Texture tUpR = new Texture("sprites/player_uphill_right.png");


    public static void renderPlayerCar(SpriteBatch batch, Segment curr,float resolution, int roadwidth,float speedPercent,float scale,int destX, int destY){
        Texture t;
        float bounce = (float) ((1.5+Math.random()*speedPercent*resolution)* Util.randomChoice(new int[]{1, -1}));
        if(curr.getP1().getWorld().getY()<curr.getP2().getWorld().getY()){
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                t=tUpL;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)){
                t=tUpR;
            }else {
                t=tUpS;
            }
        }else{
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                t=tL;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                t=tR;
            }else {
                t=tS;
            }
        }
        SpritesRenderer.render(batch,resolution,roadwidth,t,scale,destX,destY+bounce,-0.5f,-1f,0);
    }
}
