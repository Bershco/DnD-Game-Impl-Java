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

    public boolean swap(Tile t, Action a) {
        if (t.accept(this)) {
            switch (a) {
                case UP -> {
                    above = t.above;
                    t.above = this;
                    t.below = below;
                    below = t;
                    Tile tLeft = t.onTheLeft;
                    Tile tRight = t.onTheRight;
                    t.onTheRight = onTheRight;
                    t.onTheLeft = onTheLeft;
                    onTheLeft = tLeft;
                    onTheRight = tRight;
                }
                case LEFT -> {
                    onTheLeft = t.onTheLeft;
                    t.onTheLeft = this;
                    t.onTheRight = onTheRight;
                    onTheRight = t;
                    Tile tAbove = t.above;
                    Tile tBelow = t.below;
                    t.above = above;
                    t.below = below;
                    above = tAbove;
                    below = tBelow;
                }
                case DOWN -> {
                    below = t.below;
                    t.below = this;
                    t.above = above;
                    above = t;
                    Tile tLeft = t.onTheLeft;
                    Tile tRight = t.onTheRight;
                    t.onTheRight = onTheRight;
                    t.onTheLeft = onTheLeft;
                    onTheLeft = tLeft;
                    onTheRight = tRight;
                }
                case RIGHT -> {
                    onTheRight = t.onTheRight;
                    t.onTheRight = this;
                    t.onTheLeft = onTheLeft;
                    onTheLeft = t;
                    Tile tAbove = t.above;
                    Tile tBelow = t.below;
                    t.above = above;
                    t.below = below;
                    above = tAbove;
                    below = tBelow;
                }
            }

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

    public void setSurroundings(Tile[] surroundings) {
        above = surroundings[0];
        onTheLeft = surroundings[1];
        below = surroundings[2];
        onTheRight = surroundings[3];
    }
}
