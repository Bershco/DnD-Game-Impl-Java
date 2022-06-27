package Backend;


import java.util.LinkedList;
import java.util.List;

public class Player extends Unit implements HeroicUnit,Observable{
    private int experience;
    protected int playerLevel;
    private final int experiencePerLevel = 50;
    List<DeathObserver> deathObservers = new LinkedList<>();
    List<WinObserver> winObservers = new LinkedList<>();

    public Player(String _name, int _healthPool, int _attackPoints, int _defensePoints, int x, int y) {
        super(_name,'@',_healthPool,_attackPoints,_defensePoints,x,y);
        experience = 0;
        playerLevel = 1;
    }

    protected void onLevelUp() {
        experience -= playerLevel * experiencePerLevel;
        playerLevel++;
        healthPool += playerLevel * 4;
        healthAmount = healthPool;
        attackPoints += playerLevel * 4;
        defensePoints += playerLevel;
    }

    protected Action onGameTick(Action a) {
        if(experience >= playerLevel * experiencePerLevel)
            onLevelUp();
        switch (a) {
            case UP -> {
                return (swap(getAbove()) ? a : Action.STAND);
            }
            case DOWN -> {
                return (swap(getBelow()) ? a : Action.STAND);
            }
            case LEFT -> {
                return (swap(getOnTheLeft()) ? a : Action.STAND);
            }
            case RIGHT -> {
                return (swap(getOnTheRight()) ? a : Action.STAND);
            }
            case STAND -> {
                return a;
            }
            default -> throw new IllegalArgumentException("Something went wrong while performing onGameTick.");
        }
    }

    /**
     * This method is practically redundant and is for making sure each sub-class will override it
     * and implement it differently according to their abilities.
     */
    @Override
    public void castAbility(List<? extends Unit> enemies) {}

    /**
     * This method is part of the combat system of the game, it is used to attack another unit (primarily enemies)
     * @param target The unit to attack
     */

    protected void dealDamage(Enemy target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            addExp(target.getExperienceValue());
            Tile newTarget = target.enemyDeath();
            swap(newTarget);
        }
    }


    /**
     * This method describes the death of the player - simply calling the method gameOverLose() in our singleton board - in order to finish the game
     */
    @Override
    protected Tile death() {
        notifyDeathObservers();
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

    public void swap(Enemy e) {
        dealDamage(e);
    }

    @Override
    public String description() {
        return super.description() +
            "Experience: " + experience + "/" + experiencePerLevel*playerLevel + "\n" +
            "Level: " + playerLevel + "\n";
    }

    @Override
    public void addDeathObserver(DeathObserver o) {
        deathObservers.add(o);
    }

    @Override
    public void addWinObserver(WinObserver o) {
        winObservers.add(o);
    }

    @Override
    public void notifyDeathObservers() {
        for (DeathObserver o : deathObservers)
            o.onPlayerEvent();
    }

    @Override
    public void notifyWinObservers(boolean endGame) {
        for (WinObserver o : winObservers)
            o.onWinEvent(endGame);
    }
}
