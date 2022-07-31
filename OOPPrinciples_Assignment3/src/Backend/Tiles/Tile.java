package Backend.Tiles;

import Backend.Action;
import Backend.Position;
import Backend.Visited;

public abstract class Tile implements Visited {
    private final char tile;
    private Position pos;
    private Tile above;
    private Tile below;
    private Tile onTheRight;
    private Tile onTheLeft;

    //Constructor
    public Tile(char _tile, Position _pos) {
        tile = _tile;
        pos = _pos;
    }

    //Getters
    public Position getPos() {
        return pos;
    }
    public int getY() { return pos.getY();}
    public int getX() { return pos.getX();}
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
    public Tile[] getSurroundings() {
        Tile[] output = new Tile[4];
        output[0] = above;
        output[1] = onTheLeft;
        output[2] = below;
        output[3] = onTheRight;
        return output;
    }
    public char getTile() {
        return tile;
    }

    //Setters
    public void setPos(int x, int y) {
        pos = new Position(x,y);
    }
    public void setPos(Position _pos) {
        pos = _pos;
    }
    public void setSurroundings(Tile[] surroundings) {
        above = surroundings[0];
        onTheLeft = surroundings[1];
        below = surroundings[2];
        onTheRight = surroundings[3];
    }

    /**
     * This method describes a euclidean distance calculation for the purpose of finding a range between
     * 'this' tile and another tile
     * @param other the tile we are looking the range towards
     * @return a double representing the distance from 'this' tile to another
     */
    protected double range(Tile other) {
        return Math.sqrt(Math.pow((getX() - other.getX()),2)+Math.pow((getY() - other.getY()),2));
    }
    /**
     * This method describes a euclidean distance calculation for the purpose of finding a range between
     * 'this' tile and a specific position
     * @param _pos the position we are looking the range towards
     * @return a double representing the distance from 'this' tile to another
     */
    protected double range(Position _pos) {
        return Math.sqrt(Math.pow((getX() - _pos.getX()),2)+Math.pow((getY() - _pos.getY()),2));
    }
    /**
     * This method swaps the position field between 'this' Tile and another
     * @param t the other tile to switch positions with
     */
    protected void swapPositionsWith(Tile t) {
        Position tPos = t.getPos();
        t.setPos(pos);
        setPos(tPos);
    }
    /**
     * This method swaps the surroundings of 'this' Tile and another
     * @param t the other tile to swap surroundings with
     */
    protected void swapSurroundingsWith(Tile t) {
        int dx = t.getX() - getX(); //RIGHT   = +1  | LEFT = -1 | UP/DOWN    = 0
        int dy = t.getY() - getY(); //DOWN    = +1  | UP   = -1 | RIGHT/LEFT = 0
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
    /**
     * This method sets a certain Tile to be one of the surroundings of 'this' Tile
     * @param t the tile to be recognized
     * @param a the relation to 'this'
     */
    protected void acknowledge(Tile t, Action a) {
        switch (a) {
            case UP -> above = t;
            case LEFT -> onTheLeft = t;
            case DOWN -> below = t;
            case RIGHT -> onTheRight = t;
        }
    }
    /**
     * This method uses the method above to update all the surroundings that 'this' Tile is now it and not another
     */
    public void updateTheSurroundings() {
        above.acknowledge(this,Action.DOWN);
        onTheLeft.acknowledge(this, Action.RIGHT);
        below.acknowledge(this,Action.UP);
        onTheRight.acknowledge(this,Action.LEFT);
    }
    public String toString() {
        return "" + tile;
    }
}
