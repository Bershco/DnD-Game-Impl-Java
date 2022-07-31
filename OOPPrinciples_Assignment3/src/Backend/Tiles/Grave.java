package Backend.Tiles;

import Backend.Position;
import Backend.Visitor;

public class Grave extends Tile {
    public Grave(Position _pos) {
        super('X', _pos);
    }

    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }
}