package Backend;

public class Wall extends Tile {
    //Constructor
    public Wall(Position _pos) {
        super('#', _pos);
    }

    //Visitor Pattern
    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }
}