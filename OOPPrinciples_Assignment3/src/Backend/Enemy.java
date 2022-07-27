package Backend;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Enemy extends Unit implements Observable{

    private final int experienceValue;
    private final List<DeathObserver> observers = new LinkedList<>();

    protected int getExperienceValue() {
        return experienceValue;
    }


    public Enemy(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue, int x, int y) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints, x, y);
        experienceValue = _experienceValue;
    }
    /**
     * This method is part of the combat system of the game, it is used to attack another unit (primarily the player)
     * @param target The unit to attack
     */
    @Override
    protected void dealDamage(Unit target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            target.death(this);
        }
    }

    public int getExp() {
        return experienceValue;
    }

    public Action onGameTick(List<Unit> enemiesOfEnemy) {
        return Action.STAND;
    } //Because the enemy of my enemy is my friend.
    /**
     * This method describes a death of an enemy
     * @param killer
     */

    @Override
    protected void death(Unit killer) {
        notifyDeathObservers(killer,pos);
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


    @Override
    public String description() {
        return super.description() +
            "Experience Value: " + experienceValue + "\n";
    }

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
}
