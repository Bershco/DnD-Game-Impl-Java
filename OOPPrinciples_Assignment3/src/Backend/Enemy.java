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


    public Enemy(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints);
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
            target.death();
        }
    }

    public void onGameTick(List<Unit> enemiesOfEnemy) {} //Because the enemy of my enemy is my friend.
    /**
     * This method describes a death of an enemy
     */
    protected void enemyDeath() {
        notifyObservers();
    }

    /**
     * This method attempts movement of the enemy to a certain direction, followed by the following options:
     *      1. If the attempted tile is empty, simply move
     *      2. If the attempted tile is the player, attempt fighting sequence
     *      3. If the attempted tile is a wall, just randomly move to a different direction
     * @param d the attempted direction to move towards
     */
    @Override
    protected void move(Direction d) {
        switch (d) {
            case UP -> swap(getAbove());
            case DOWN -> swap(getBelow());
            case LEFT -> swap(getOnTheLeft());
            case RIGHT -> swap(getOnTheRight());
        }
    }

    /**
     * This method moves the enemy randomly using a simple Math.random method call
     */
    protected void randomizeMove() {
        Random random = new Random();
        int direction = random.nextInt(5);
        switch (direction) {
            case 0 -> move(Direction.UP);
            case 1 -> move(Direction.LEFT);
            case 2 -> move(Direction.DOWN);
            case 3 -> move(Direction.RIGHT);
            case 4 -> move(Direction.STAND);
        }

    }

    @Override
    public String description() {
        return super.description() +
            "Experience Value: " + experienceValue + "\n";
    }

    @Override
    public void addObserver(DeathObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (DeathObserver observer : observers)
            observer.onEnemyEvent(this);
    }
}
