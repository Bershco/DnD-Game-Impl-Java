public class Unit {
    public String name;
    public int healthPool;
    public int healthAmunt;
    public int attackPoints;
    public int defensePoints;

    public String getname() {
        return name;
    }

    public String description() {
        throw new UnsupportedOperationException("No implementation yet");
    }

    public String toString() {
        return super.toString();
    }

    public void move() {
    }

    public void onGameTick() {
    }
}
