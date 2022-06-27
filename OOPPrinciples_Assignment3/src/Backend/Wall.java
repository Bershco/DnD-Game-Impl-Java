package Backend;

public class Wall extends Tile {
    public Wall() {
        super('#');
    }

    @Override
    public boolean accept(Tile t) {
        return false;
    }
}