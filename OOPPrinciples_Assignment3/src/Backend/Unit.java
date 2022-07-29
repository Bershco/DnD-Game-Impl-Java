package Backend;

public class Unit extends Tile implements Visitor{
    private final String name;
    protected int healthPool;
    protected int healthAmount;
    protected int attackPoints;
    protected int defensePoints;
    protected MessageCallback messageCallback;

    public String getName() {
        return name;
    }

    public int getAttackPoints() { return attackPoints; }
    public int getDefensePoints() { return defensePoints; }

    public Unit(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int x, int y) {
        super(_tile, new Position(x,y));
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
        return name + ":\t\t" +
                "Health: " + healthAmount + "/" + healthPool + "\t\t" +
                "Attack: " + attackPoints + "\t\t" +
                "Defense: " + defensePoints + "\t\t";
    }

    @Override
    public boolean visit(Player p) {
        return false;
    }

    @Override
    public boolean visit(Enemy e) {
        return false;
    }

    @Override
    public boolean visit(Empty e) {
        if (e.accept(this)) {
            swapSurroundingsWith(e);
            swapPositionsWith(e);
            return true;
        }
        return false;
    }

    @Override
    public boolean visit(Wall w) {
        if (w.accept(this)) {
            swapSurroundingsWith(w);
            swapPositionsWith(w);
            return true;
        }
        return false;
    }

    @Override
    public boolean visit(Grave g) {
        if (g.accept(this)) {
            swapSurroundingsWith(g);
            swapPositionsWith(g);
            return true;
        }
        return false;
    }

    public String toString() {
        return super.toString();
    }

    /**
     * This method moves a unit
     * @param d the direction to attempt movement towards
     */
    protected Action move(Action d) { return d;}

    /**
     * This method describes a death of a unit
     * @param killer
     */
    protected void death(Unit killer) {}

    public int getHealthAmount() {
        return healthAmount;
    }

    public void setMessageCallback(MessageCallback _messageCallback){
        messageCallback = _messageCallback;
    }

    /**
     * This method is part of the combat system of the game, it is used to attack another unit
     * @param target The unit to attack
     */
    protected void dealDamage(Unit target) {
        int attackRoll = (int)Math.round(Math.random()*(attackPoints+1));
        int defenseRoll = (int)Math.round(Math.random()*(target.defensePoints+1));
        int damage = attackRoll - defenseRoll;
        if (damage > 0) {
            target.healthAmount -= damage;
        }
        messageCallback.send(generateBattleSequence(this,target,damage));

    }

    public String generateBattleSequence(Unit attacker, Unit defender, int damage) {
        StringBuilder output = new StringBuilder("=======================================\n" +
                attacker.getName() + "\t\t VS \t" + defender.getName() + "\n" +
                "Attack: \t" + attacker.getAttackPoints() + "\t\t\t" + "Defense: \t" + defender.getDefensePoints() + "\n" +
                "Roll results: \t" + damage + "\t\t" + "Health: " +((damage>0) ? defender.getHealthAmount()+damage : defender.getHealthAmount()) + "\n" +
                "=======================================\n");
        if (damage > 0)
            return output +
                    attacker.getName() + " has inflicted " + damage + " damage to " + defender.getName() + "\n" +
                    defender.getName() + "'s health points are now: " + defender.getHealthAmount();
        else
            return output +
                    attacker.getName() + " has tried to damage " + defender.getName() + " with no success.";
    }


    public boolean accept(Unit u) {
        u.dealDamage(this);
        return false;
    }
}
