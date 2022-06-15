public class Unit extends Tile{
    public String name;
    public int healthPool;
    public int healthAmount;
    public int attackPoints;
    public int defensePoints;

    public String getname() {
        return name;
    }

    /**
     * This method returns full information of the current unit.
     * Mainly used to print the information of each unit during combat / on player's turn
     * @return
     */
    public String description() {
        throw new UnsupportedOperationException("No implementation yet");
    }

    public String toString() {
        return super.toString();
    }

    /**
     * This method moves a unit
     * @param d the direction to attempt movement towards
     */
    public void move(Direction d) {
    }

    /**
     * This method describes a unit's play when the turn is theirs
     */
    public void onGameTick() {}

    /**
     * This method describes a death of a unit
     */
    public void death() {}

    /**
     * This method is part of the combat system of the game, it is used to attack another unit
     * @param target The unit to attack
     */
    public void dealDamage(Unit target) {
        int attackRoll = (int)Math.round(Math.random()*(attackPoints+1));
        int defenseRoll = (int)Math.round(Math.random()*(target.defensePoints+1));
        int damage = attackRoll - defenseRoll;
        if (damage > 0)
            target.healthAmount -= damage;
    }
}
