public class Unit extends Tile{
    public String name;
    public int healthPool;
    public int healthAmount;
    public int attackPoints;
    public int defensePoints;

    public String getName() {
        return name;
    }

    public String description() {
        return "" + name + ":\n" +
                "Health: " + healthAmount + " out of " + healthPool + "\n" +
                "Attack: " + attackPoints + "\n" +
                "Defense: " + defensePoints + "\n";
    }

    public String toString() {
        return super.toString();
    }

    public void move(Direction d) {
    }
    public void onGameTick() {}

    public void death() {}

    public void dealDamage(Unit target) {
        int attackRoll = (int)Math.round(Math.random()*(attackPoints+1));
        int defenseRoll = (int)Math.round(Math.random()*(target.defensePoints+1));
        int damage = attackRoll - defenseRoll;
        if (damage > 0)
            target.healthAmount -= damage;
        if (target.healthAmount <= 0)
            target.death();
    }
}
