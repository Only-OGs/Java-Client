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

    public static double interpolate(double a, double b, double percent) {
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

    public static boolean overlap(double x1, double w1, double x2, double w2, double percent) {
        double half = percent;
        if(percent >= 1) {
            half = 0.5f;
        }

        double left1 = x1 - (w1 * half);
        double right1 = x1 + (w1 * half);
        double left2 = x2;
        double right2 = x2 + w2;

        boolean leftIn = left1 >= left2 && left1 <= right2;
        boolean rightIn = right1 <= right2 && right1 >= left2;

        return leftIn || rightIn;
<<<<<<< HEAD
    }

    public static int closer(int num1, int num2, int target) {
        return Math.abs(target - num1) <= Math.abs(target - num2) ? num1 : num2;
    }

    public static String formatTimer(float timer) {
        String timeString = "";
        int minutes = (int)timer / 60;
        int seconds = (int)timer % 60;
        int millis = (int) (timer % 1 * 10);
        timeString += minutes > 0 ? minutes + "." : "0.";
        timeString += seconds > 0 ? seconds + "." : "0.";
        timeString += millis > 0 ? millis + "" : "0";

        return timeString;
    }

    public static String formatSpeed(float speed, float maxSpeed) {
        int formatedSpeed = 0;
        int step = 5;
        int s = (int)speed;
        for(int i = 0; i < (int)(speed/step); i++){
            if(s - step >= 0) {
                formatedSpeed += step;
                s -= step;
            }
        }
        return ""+closer(formatedSpeed, (int)maxSpeed, (int)speed);
=======
>>>>>>> 5fa8f285abd115ab3e513b3b1c2a6559d6b993fd
    }
}
