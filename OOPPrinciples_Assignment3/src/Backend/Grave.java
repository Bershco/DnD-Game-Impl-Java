package Backend;

public class Grave extends Tile {
    public Grave(Position _pos) {
        super('X');
        pos = _pos;
    }

    @Override
    public boolean accept(Tile t) {
        return false;
    }
}
