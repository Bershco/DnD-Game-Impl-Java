package Backend.Tiles;

import Backend.Position;
import Backend.Visitor;

public class Grave extends Tile {
    //Constructor
    public Grave(Position _pos) {
        super('X', _pos);
    }

    //Visitor Pattern
    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
