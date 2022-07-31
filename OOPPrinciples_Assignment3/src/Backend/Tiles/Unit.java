package Backend.Tiles;

import Backend.*;

public abstract class Unit extends Tile implements Visitor {
    private final String name;
    private int healthPool;
    private int healthAmount;
    private int attackPoints;
    private int defensePoints;
    protected MessageCallback messageCallback;

    //Constructor
    public Unit(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int x, int y) {
        super(_tile, new Position(x,y));
        name = _name;
        healthPool = _healthPool;
        healthAmount = healthPool;
        attackPoints = _attackPoints;
        defensePoints = _defensePoints;
    }

    //Getters
    public String getName() {
        return name;
    }
    public int getAttackPoints() { return attackPoints; }
    public int getDefensePoints() { return defensePoints; }
    public int getHealthAmount() {
        return healthAmount;
    }
    public int getHealthPool() {
        return healthPool;
    }

    //Setters
    public void setMessageCallback(MessageCallback _messageCallback){
        messageCallback = _messageCallback;
    }
    public void fullHealth() {healthAmount = healthPool;}
    public void setHealthAmount(int healthAmount) {
        this.healthAmount = healthAmount;
    }
    public void alterHealthAmountBy(int i) {
        healthAmount += i;
    }
    public void raiseHealthPoolBy(int i) {
        healthPool += i;
    }
    public void raiseAttackPoints(int i) {
        attackPoints += i;
    }
    public void raiseDefensePoints(int i) {
        defensePoints += i;
    }
    protected void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    //Abstract Methods
    public abstract void death(Unit killer);
    public abstract int getExperienceValue();

    /**
     * This method is part of the combat system of the game, it is used to attack another unit
     * @param target The unit to attack
     */
    protected boolean dealDamage(Unit target) {
        int attackRoll = (int)Math.round(Math.random()*(attackPoints+1));
        int defenseRoll = (int)Math.round(Math.random()*(target.defensePoints+1));
        int damage = attackRoll - defenseRoll;
        if (damage > 0) {
            target.healthAmount -= damage;
        }
        messageCallback.send(generateBattleSequence(this,target,damage));
        return target.healthAmount <= 0;
    }

    /**
     * This method generates battle sequences between the attacker and the defender (not the actual calculations, rather - the output string)
     * @param attacker the attacking unit
     * @param defender the defending unit
     * @param damage the damage taken
     * @return the generated String to output
     */
    private String generateBattleSequence(Unit attacker, Unit defender, int damage) {
        StringBuilder output = new StringBuilder("=======================================\n" +
                attacker.description() + "\n\t\t\tATTACKS\n" + defender.description()+
                "\nRoll results: \t" + damage + "\t\t" + "Health: " +((damage>0) ? defender.getHealthAmount()+damage : defender.getHealthAmount()) + "\n" +
                "=======================================\n");
        if (damage > 0) {
            if (defender.getHealthAmount() > 0)
                return output +
                        attacker.getName() + " has inflicted " + damage + " damage to " + defender.getName() + "\n" +
                        defender.getName() + "'s health points are now: " + defender.getHealthAmount();
            else
                return output +
                        attacker.getName() + " has inflicted " + damage + " damage to " + defender.getName() + "\n" +
                        defender.getName() + " is now dead";
        }
        else
            return output +
                    attacker.getName() + " has tried to damage " + defender.getName() + " with no success.";
    }

    /**
     * This method returns full information of the current unit.
     * @return description of the unit
     */
    public String description() {
        return name + ":\t\t" +
                "Health: " + healthAmount + "/" + healthPool + "\t\t" +
                "Attack: " + attackPoints + "\t\t" +
                "Defense: " + defensePoints + "\t\t";
    }

    /**
     * This method moves a unit to a certain tile using the visitor pattern and updates their fields properly
     * @param t the tile the unit attempts to move towards
     * @return whether the movement was successful or not
     */
    public boolean goTo(Tile t) {
        if (t.accept(this)) {
            swapSurroundingsWith(t);
            swapPositionsWith(t);
            return true;
        }
        return false;
    }

    //Visitor Pattern
    @Override
    public boolean visit(Empty e) {
        return true;
    }

    @Override
    public boolean visit(Wall w) {
        return false;
    }

    @Override
    public boolean visit(Grave g) {
        return false;
    }


    public String toString() {
        return super.toString();
    }
}