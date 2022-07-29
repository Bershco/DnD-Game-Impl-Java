package Backend;

public abstract class Tile implements Visited{
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
        return Math.sqrt(Math.pow((pos.x - other.pos.x),2)+Math.pow((pos.y - other.pos.y),2));
    }
    protected double range(Position _pos) {
        return Math.sqrt(Math.pow((pos.x - _pos.x),2)+Math.pow((pos.y - _pos.y),2));
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

    public void swapPositionsWith(Tile t) {
        Position tPos = t.pos;
        t.pos = pos;
        pos = tPos;
    }

    public void swapSurroundingsWith(Tile t) {
        int dx = t.pos.x - pos.x; //RIGHT   = +1  | LEFT = -1 | UP/DOWN    = 0
        int dy = t.pos.y - pos.y; //DOWN    = +1  | UP   = -1 | RIGHT/LEFT = 0
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            //Teleportation isn't implemented but we put this in anyways
            Tile[] temp = new Tile[4];
            temp[0] = t.getAbove();
            temp[1] = t.getOnTheLeft();
            temp[2] = t.getBelow();
            temp[3] = t.getOnTheRight();
            t.setSurroundings(getSurroundings());
            setSurroundings(temp);
        }
        else { //Now we're walking
            Tile[] tempT = t.getSurroundings();
            Tile[] tempThis = getSurroundings();
            if (dx == 0) {
                if (dy == -1) {
                    tempT[2] = t;
                    tempThis[0] = this;
                }
                else if (dy == 1) {
                    tempT[0] = t;
                    tempThis[2] = this;
                }
            }
            else {
                if (dx == -1) {
                    tempT[3] = t;
                    tempThis[1] = this;
                }
                else if (dx == 1) {
                    tempT[1] = t;
                    tempThis[3] = this;
                }
            }
            t.setSurroundings(tempThis);
            setSurroundings(tempT);
        }
        t.updateTheSurroundings();
        updateTheSurroundings();
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

    public Tile[] getSurroundings() {
        Tile[] output = new Tile[4];
        output[0] = above;
        output[1] = onTheLeft;
        output[2] = below;
        output[3] = onTheRight;
        return output;
    }

    public void acknowledge(Tile t, Action a) {
        switch (a) {
            case UP -> above = t;
            case LEFT -> onTheLeft = t;
            case DOWN -> below = t;
            case RIGHT -> onTheRight = t;
        }
    }

    public void updateTheSurroundings() {
        above.acknowledge(this,Action.DOWN);
        onTheLeft.acknowledge(this, Action.RIGHT);
        below.acknowledge(this,Action.UP);
        onTheRight.acknowledge(this,Action.LEFT);
    }
}
