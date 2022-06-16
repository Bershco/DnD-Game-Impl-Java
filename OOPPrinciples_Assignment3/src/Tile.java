public class Tile {
    public char tile;
    public Position pos;


    public Tile(char _tile) {
        tile = _tile;
    }
    /**
     * This method describes a euclidean distance calculation for the purpose of finding a range between
     * 'this' tile and another tile
     * @param other the tile we are looking the range towards
     * @return a double representing the distance from 'this' tile to another
     */
    public double range(Tile other) {
        return Math.sqrt(Math.pow((pos.x - other.pos.x),2)+Math.pow((pos.x - other.pos.x),2));
    }

    public String toString() {
        return "" + tile;
    }
}
