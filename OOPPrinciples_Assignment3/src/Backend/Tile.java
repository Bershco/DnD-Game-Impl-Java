package Backend;

public class Tile {
    protected char tile;
    protected Position pos;
    private Tile above;
    private Tile below;
    private Tile onTheRight;
    private Tile onTheLeft;

    public Tile(char _tile, Position _pos) {
        tile = _tile;
        pos = _pos;
    }
    protected void setPos(int x, int y) {
        pos = new Position(x,y);
    }
    /**
     * This method describes a euclidean distance calculation for the purpose of finding a range between
     * 'this' tile and another tile
     * @param other the tile we are looking the range towards
     * @return a double representing the distance from 'this' tile to another
     */
    protected double range(Tile other) {
        return Math.sqrt(Math.pow((pos.x - other.pos.x),2)+Math.pow((pos.x - other.pos.x),2));
    }
    protected double range(Position _pos) {
        return Math.sqrt(Math.pow((pos.x - _pos.x),2)+Math.pow((pos.x - _pos.x),2));
    }

    public char getTile() {
        return tile;
    }
    public Position getPos() {
        return pos;
    }
    public Tile getAbove() {
        return above;
    }
    public Tile getBelow() {
        return below;
    }
    public Tile getOnTheLeft() {
        return onTheLeft;
    }
    public Tile getOnTheRight() {
        return onTheRight;
    }

    public boolean swap(Tile t) {
        if (t.accept(this)) {
            Tile tUp = t.above;
            Tile tDown = t.below;
            Tile tRight = t.onTheRight;
            Tile tLeft = t.onTheLeft;
            t.above = above;
            t.below = below;
            t.onTheRight = onTheRight;
            t.onTheLeft = onTheLeft;
            above = tUp;
            below = tDown;
            onTheRight = tRight;
            onTheLeft = tLeft;

            Position tPos = t.pos;
            t.pos = pos;
            pos = tPos;
            return true;
        }
        return false;
    }


    public boolean accept(Tile t) {
        return true;
    }
    public String toString() {
        return "" + tile;
    }
}
