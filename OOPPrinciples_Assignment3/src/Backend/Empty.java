package Backend;

public class Empty extends Tile {
    public Empty(Position _pos) {
        super('.', _pos);
    }

    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }
}