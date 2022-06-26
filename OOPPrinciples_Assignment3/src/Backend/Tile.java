package Backend;

public class Tile {
    protected char tile;
    protected Position pos;


    public Tile(char _tile) {
        tile = _tile;
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

    public char getTile() {
        return tile;
    }
    public Position getPos() {
        return pos;
    }
    public String toString() {
        return "" + tile;
    }
}
