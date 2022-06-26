package Backend;

public class Unit extends Tile{
    private final String name;
    protected int healthPool;
    protected int healthAmount;
    protected int attackPoints;
    protected int defensePoints;

    protected String getName() {
        return name;
    }

    public Unit(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints) {
        super(_tile);
        name = _name;
        healthPool = _healthPool;
        healthAmount = healthPool;
        attackPoints = _attackPoints;
        defensePoints = _defensePoints;
    }

    /**
     * This method returns full information of the current unit.
     * Mainly used to print the information of each unit during combat / on player's turn
     * @return description of the unit
     */
    public String description() {
        return "" + name + ":\n" +
                "Health: " + healthAmount + " out of " + healthPool + "\n" +
                "Attack: " + attackPoints + "\n" +
                "Defense: " + defensePoints + "\n";
    }

    public String toString() {
        return super.toString();
    }

    /**
     * This method moves a unit
     * @param d the direction to attempt movement towards
     */
    protected void move(Direction d) {
    }

    /**
     * This method describes a unit's play when the turn is theirs
     */
    protected void onGameTick() {}

    /**
     * This method describes a death of a unit
     */
    protected void death() {}

    protected void register() {

    }

    public int getHealthAmount() {
        return healthAmount;
    }

    /**
     * This method is part of the combat system of the game, it is used to attack another unit
     * @param target The unit to attack
     */
    protected void dealDamage(Unit target) {
        int attackRoll = (int)Math.round(Math.random()*(attackPoints+1));
        int defenseRoll = (int)Math.round(Math.random()*(target.defensePoints+1));
        int damage = attackRoll - defenseRoll;
        if (damage > 0)
            target.healthAmount -= damage;
        if (target.healthAmount <= 0)
            target.death();
    }
}
