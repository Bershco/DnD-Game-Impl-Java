package Backend;


import java.util.LinkedList;
import java.util.List;

public class Player extends Unit implements HeroicUnit,Observable{
    private int experience;
    protected int playerLevel;
    List<DeathObserver> observers = new LinkedList<>();

    public Player(String _name, int _healthPool, int _attackPoints, int _defensePoints) {
        super(_name,'@',_healthPool,_attackPoints,_defensePoints);
        experience = 0;
        playerLevel = 1;
    }

    protected void onLevelUp() {
        experience -= playerLevel * 50;
        playerLevel++;
        healthPool += playerLevel * 4;
        healthAmount = healthPool;
        attackPoints += playerLevel * 4;
        defensePoints += playerLevel;
    }

    protected void onGameTick() {
        //TODO: implement observer pattern to check events for user entered action
        if(experience >= playerLevel * 50)
            onLevelUp();
    }

    /**
     * This method is practically redundant and is for making sure each sub-class will override it
     * and implement it differently according to their abilities.
     */
    @Override
    public void castAbility(List<Unit> enemies) {}

    /**
     * This method is part of the combat system of the game, it is used to attack another unit (primarily enemies)
     * @param target The unit to attack
     */

    protected void dealDamage(Enemy target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            addExp(target.getExperienceValue());
            Tile newTarget = target.death();
            swap(newTarget);
        }
    }


    /**
     * This method describes the death of the player - simply calling the method gameOverLose() in our singleton board - in order to finish the game
     */
    @Override
    protected Tile death() {
        notifyObservers();
        return new Grave(pos);
    }
    /**
     * This method describes how experience is added to the player
     */
    protected void addExp(int exp) {
        experience += exp;
    }

    /**
     * This method dictates if the player has enough resources to use his ability
     * because this method is for a player - which should not be instanced at all, it always returns true
     * @return true
     */
    protected boolean enoughResources() {
        return true;
    }

    @Override
    public boolean accept(Unit t) {
        t.dealDamage(this);
        return false;
    }

    @Override
    public String description() {
        return super.description() +
            "Experience: " + experience + "\n" +
            "Level: " + playerLevel + "\n";
    }

    @Override
    public void addObserver(DeathObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (DeathObserver o : observers) {
            o.onPlayerEvent();
        }
    }
}
