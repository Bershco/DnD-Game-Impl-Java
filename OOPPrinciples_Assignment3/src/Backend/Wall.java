package Backend;

public class Wall extends Tile {
    public Wall(Position _pos) {
        super('#', _pos);
    }

    @Override
    public boolean accept(Tile t) {
        return false;
    }
}