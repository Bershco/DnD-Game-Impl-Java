package Backend;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Enemy extends Unit implements Observable{

    private final int experienceValue;
    private final List<DeathObserver> observers = new LinkedList<>();
    public int getExperienceValue() {
        return experienceValue;
    }

    //Constructor
    public Enemy(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue, int x, int y) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints, x, y);
        experienceValue = _experienceValue;
    }

    /**
     * This method is overridden in each extending class, and if somehow it reached here we need it to return Action.STAND
     * @param enemiesOfEnemy a list of the player without knowing the player
     * @return only Action.STAND
     */
    protected Action onGameTick(List<Unit> enemiesOfEnemy) {//Because the enemy of my enemy is my friend.
        return Action.STAND;
    }

    /**
     * This method describes a death of an enemy
     * @param killer the killer of 'this'
     */
    @Override
    protected void death(Unit killer) {
        notifyDeathObservers(killer,getPos());
    }

    /**
     * This method moves the enemy randomly using a simple Math.random method call
     */
    protected Action randomizeMove() {
        Random random = new Random();
        int direction = random.nextInt(5);
        switch (direction) {
            case 0 -> {
                return Action.UP;
            }
            case 1 -> {
                return Action.LEFT;
            }
            case 2 -> {
                return Action.DOWN;
            }
            case 3 -> {
                return Action.RIGHT;
            }
            default -> { //this is for "4" and for compilation.
                return Action.STAND;
            }
        }
    }

    /**
     * This method adds description to Unit's description and returns it as String
     * @return String form description of 'this' Enemy
     */
    @Override
    public String description() {
        return super.description() +
            "Experience Value: " + experienceValue + "\t";
    }

    //Observer Pattern
    @Override
    public void addDeathObserver(DeathObserver o) {
        observers.add(o);
    }
    @Override
    public void addWinObserver(WinObserver o) {} //Won't do anything, monsters can't win unless the player loses, and that's just dumb.
    @Override
    public void notifyDeathObservers(Unit Killer, Position DeathPos) {
        for (DeathObserver observer : observers)
            observer.onEnemyEvent(this);
    }
    @Override
    public void notifyWinObservers(boolean endGame) {} //Won't do anything, monsters can't win unless the player loses, and that's just dumb.


    //Visitor Pattern
    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public boolean visit(Enemy e) {
        return false;
    }
    @Override
    public boolean visit(Player p) {
        if (dealDamage(p)) {
            p.death(this);
        }
        return false;
    }
}
