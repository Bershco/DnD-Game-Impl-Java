public class Tile {
    public char tile;
    public Position pos;
    public double range(Tile other) {
        return Math.sqrt(Math.pow((pos.x - other.pos.x),2)+Math.pow((pos.x - other.pos.x),2));
    }

    public String toString() {
        return ""+ tile;
    }
}
