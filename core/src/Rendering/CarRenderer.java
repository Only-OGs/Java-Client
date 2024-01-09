package Rendering;

import Helpers.Util;
import OGRacerGame.OGRacerGame;
import Road.Helper.Segment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CarRenderer {
    /**
     * Klasse, die Hauptsächlich entscheidet, welches Sprite für den Player genutzt werden soll und dieses dann mithilfe
     * des SpriteRenderer zeichnet. Wird von der Render Klasse aufgerufen.
     * @param batch
     * @param curr
     * @param resolution
     * @param roadwidth
     * @param speedPercent
     * @param scale
     * @param destX
     * @param destY
     */
    public static void renderPlayerCar(SpriteBatch batch, Segment curr,float resolution, int roadwidth,float speedPercent,float scale,int destX, int destY){
        Texture t;
        float bounce = (float) ((1.5+Math.random()*speedPercent*resolution)* Util.randomChoice(new int[]{1, -1}))*speedPercent;
        if(curr.getP1().getWorld().getY()<curr.getP2().getWorld().getY()){
            if(Gdx.input.isKeyPressed(Input.Keys.A) && OGRacerGame.getInstance().isRunning && OGRacerGame.movement){
                t=AssetData.getplayer(3);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) && OGRacerGame.getInstance().isRunning  && OGRacerGame.movement){
                t=AssetData.getplayer(5);
            }else {
                t=AssetData.getplayer(1);
            }
        }else{
            if(Gdx.input.isKeyPressed(Input.Keys.A) && OGRacerGame.getInstance().isRunning&& OGRacerGame.movement){
                t=AssetData.getplayer(2);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) && OGRacerGame.getInstance().isRunning&& OGRacerGame.movement) {
                t=AssetData.getplayer(4);
            }else {
                t=AssetData.getplayer(0);
            }
        }
        SpritesRenderer.render(batch,resolution,roadwidth,t,scale,destX,destY+bounce,-0.5f,-1f,0);
    }
}
