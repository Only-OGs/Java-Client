package Road;

public enum Curve {
    NONE(0),
    EASY(2),
    MEDIUM(4),
    HARD(6);

    private final int value;

    Curve(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}
