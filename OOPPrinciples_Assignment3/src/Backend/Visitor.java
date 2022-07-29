package Backend;

public interface Visitor {
    public boolean visit(Player p);
    public boolean visit(Enemy e);
    public boolean visit(Empty e);
    public boolean visit(Wall w);
    public boolean visit(Grave g);
}
