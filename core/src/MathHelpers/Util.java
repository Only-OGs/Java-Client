package MathHelpers;


import Road.P;
import java.util.Date;

public class Util {


    public Util() {
    }

    long timestap() {
        return new Date().getTime();
    }

    public static int toInt(Object obj, int def) {
        if (obj != null) {
            try {
                return Integer.parseInt(obj.toString());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return def;
    }

    public static float toFloat(Object obj, float def) {
        if (obj != null) {
            try {
                return Float.parseFloat(obj.toString());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return def;
    }


    public static int randomChoice(int[] options) {
        if (options == null || options.length == 0) {
            throw new IllegalArgumentException("Option Array ist leer oder null\n");
        }
        int randomIndex = randomInt(0, options.length - 1);
        return options[randomIndex];

    }

    public static float accelerate(int v, int accel, float dt) {
        return v + (accel * dt);
    }

    public static double percentRemaining(float n, float total) {
        return (double) (n % total) / total;
    }

    public static double interpolate(int a, int b, double percent) {
        return a + (b - a) * percent;
    }

    public static int randomInt(int min, int max) {
        return (int) Math.round(interpolate(min, max, Math.random()));
    }

    public static float easeIn(float a, float b, double percent) {
        return (float)(a + (b - a) * Math.pow(percent, 2));
    }

    public static float easeOut(float a, float b, double percent) {
        return (float) (a + (b - a) * (1 - Math.pow(1 - percent, 2)));
    }

    public static float easeInOut(float a, float b, double percent) {
        return (float) (a + (b - a) * ((-Math.cos(percent * Math.PI) / 2) + 0.5));
    }

    public static double exponentialFog(double distance, double density) {
        return 1 / (Math.pow(Math.E, (distance * distance * density)));
    }

    public static int increase(int start, int increment, int max) {
        int result = start + increment;
        while (result >= max)
            result -= max;
        while (result < 0)
            result += max;
        return result;
    }

    public static void project(P p, int cameraX, int cameraY, float cameraZ, double cameraDepth, int width, int height, double roadWidth) {
        p.getCamera().setX(((p.getWorld() != null) ? p.getWorld().getX() : 0) - cameraX);
        p.getCamera().setY(((p.getWorld() != null) ? p.getWorld().getY() : 0) - cameraY);
        p.getCamera().setZ((int) (((p.getWorld() != null) ? p.getWorld().getZ() : 0) - cameraZ));
        p.getScreen().setScale(cameraDepth / p.getCamera().getZ());
        p.getScreen().setX((int) Math.round(((double) width / 2) + (p.getScreen().getScale() * p.getCamera().getX() * (width / 2))));
        p.getScreen().setY((int) Math.round(((double) height / 2) - (p.getScreen().getScale() * p.getCamera().getY() * (height / 2))));
        p.getScreen().setW((int) Math.round((p.getScreen().getScale() * roadWidth * (width / 2))));
    }

    public static float limit(float value, float min, float max) {
        return Math.max( min,  Math.min(value, max) );
    }

    public static boolean overlap(int x1, int w1, int x2, int w2, double percent) {
        double half;
        if (percent > 1) {
            half = percent / 2;
        } else {
            half = 1.0 / 2;
        }
        double min1 = x1 - (w1 * half);
        double max1 = x1 + (w1 * half);
        double min2 = x2 - (w2 * half);
        double max2 = x2 + (w2 * half);
        return !((max1 < min2) || (min1 > max2));
    }
}
