package Backend;

public class Position {
    private final int x;
    private final int y;

    //Constructor
    public Position(int _x, int _y) {
        x = _x;
        y = _y;
    }

    //Getters
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
}
