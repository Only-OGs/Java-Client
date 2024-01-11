package Road.Helper;

public enum Curve {
    NONE(0),
    EASYRIGHT(2),
    MEDIUMRIGHT(4),
    HARDRIGHT(6),
    EASYLEFT(-2),
    MEDIUMLEFT(-4),
    HARDLeft(-6);

    private final float value;

    Curve(float i) {
        value = i;
    }

    public float getValue() {
        return value;
    }
}
