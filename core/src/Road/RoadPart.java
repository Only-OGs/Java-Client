package Road;

public class RoadPart {

    public RoadPart(String lenght, String curve, String hill) {
        this.lenght = Integer.parseInt(lenght);
        this.curve = Integer.parseInt(curve);
        this.hill = Integer.parseInt(hill);
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

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public void setCurve(int curve) {
        this.curve = curve;
    }

    public void setHill(int hill) {
        this.hill = hill;
    }
}

