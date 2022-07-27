package Backend;

public class Grave extends Tile {
    public Grave(Position _pos) {
        super('X', _pos);
    }

    @Override
    public boolean accept(Unit u) {
        return false;
    }
}
