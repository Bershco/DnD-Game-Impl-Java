package Backend.Tiles;

import Backend.Position;
import Backend.Visitor;

public class Empty extends Tile {

    //Constructor
    public Empty(Position _pos) {
        super('.', _pos);
    }

    //Visitor Pattern
    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }
}