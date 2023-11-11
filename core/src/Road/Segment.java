package Road;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

public class Segment {

    private int index;
    private P p1;
    private P p2;
    private boolean looped;
    private int fog;
    private Color color;
    private float clip;

    public Segment(int index, P p1, P p2, boolean looped, float clip, int fog, Color color) {
        this.index = index;
        this.p1 = p1;
        this.p2 = p2;
        this.looped = looped;
        this.clip = clip;
        this.fog = fog;
        this.color = color;
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

    public int getFog() {
        return fog;
    }

    public void setFog(int fog) {
        this.fog = fog;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getClip() {
        return clip;
    }

    public void setClip(float clip) {
        this.clip = clip;
    }
}

