package Road.Helper;

import Road.Helper.Cam;
import Road.Helper.Screen;
import Road.Helper.World;

public class P {

    private World world;
    private Cam camera;
    private Screen screen;

    public P(World world, Cam camera, Screen screen) {
        this.world = world;
        this.camera = camera;
        this.screen = screen;
    }


    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Cam getCamera() {
        return camera;
    }

    public void setCamera(Cam camera) {
        this.camera = camera;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
