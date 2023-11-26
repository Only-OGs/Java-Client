package Road;


import Rendering.ColorThemes;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Segment {

    private int index;
    private P p1;
    private P p2;
    private boolean looped;
    private double fog;
    private Color[] color;
    private float curve;
    private CustomSprite[] sprites;
    private int clip;


    public Segment(int index, P p1, P p2, Color[] c, float curve) {
        this.index = index;
        this.p1 = p1;
        this.p2 = p2;
        this.color=c;
        this.curve=curve;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public P getP1() {
        return p1;
    }

    public void setP1(P p1) {
        this.p1 = p1;
    }

    public P getP2() {
        return p2;
    }

    public void setP2(P p2) {
        this.p2 = p2;
    }

    public boolean isLooped() {
        return looped;
    }

    public void setLooped(boolean looped) {
        this.looped = looped;
    }

    public double getFog() {
        return fog;
    }

    public void setFog(double fog) {
        this.fog = fog;
    }

    public Color[] getColor() {
        return color;
    }

    public void setColor(Color[] color) {
        this.color = color;
    }

    public float getCurve() {
        return curve;
    }

    public void setCurve(float curve) {
        this.curve = curve;
    }

    public void addSprite(CustomSprite cs){
        if(sprites!=null){
            CustomSprite[] temp = new CustomSprite[sprites.length+1];
            for(int i=0;i<sprites.length;i++){
                temp[i]= sprites[i];
            }
            temp[sprites.length]=cs;
            sprites=temp;
        }else{
            sprites = new CustomSprite[1];
            sprites[0]=cs;
        }
    }

    public CustomSprite[] getSprites() {return sprites;}

    public void setClip(int clip) {this.clip = clip;}

    public int getClip() {return clip;}
}

