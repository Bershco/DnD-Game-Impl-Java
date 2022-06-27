package Backend;

public class Wall extends Tile {
    public Wall(Position _pos) {
        super('#', _pos.x,_pos.y);
    }

    @Override
    public boolean accept(Tile t) {
        return false;
    }
}