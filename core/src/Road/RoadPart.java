package Road;

/**
 * Speichert die gesendeten Strecken Daten zwischen, damit man sie dann vom Roadbuilder aus aufrufen kann.
 */
public class RoadPart {

    public RoadPart(String lenght, String curve, String hill) {
        this.lenght = Integer.parseInt(lenght);
        this.curve = Integer.parseInt(curve);
        this.hill = (int)Float.parseFloat(hill);
    }

    private int lenght;
    private int curve;
    private int hill;

    public int getLenght() {
        return lenght;
    }

    public int getCurve() {
        return curve;
    }

    public int getHill() {
        return hill;
    }
}

