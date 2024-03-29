package Backend.Tiles.PlayerTiles;


import Backend.*;
import Backend.Tiles.EnemyTiles.Enemy;
import Backend.Tiles.Unit;

import java.util.LinkedList;
import java.util.List;

public class Player extends Unit implements HeroicUnit, Observable {
    private int experience;
    protected int playerLevel;
    private final int experiencePerLevel = 50;
    private final List<DeathObserver> deathObservers = new LinkedList<>();
    private final List<WinObserver> winObservers = new LinkedList<>();

    //Constructor
    public Player(String _name, int _healthPool, int _attackPoints, int _defensePoints, int x, int y) {
        super(_name,'@',_healthPool,_attackPoints,_defensePoints,x,y);
        experience = 0;
        playerLevel = 1;
    }

    /**
     * This method describes the process of a level up of a base player
     */
    protected void onLevelUp() {
        experience -= playerLevel * experiencePerLevel;
        playerLevel++;
        raiseHealthPoolBy(playerLevel * 4);
        fullHealth();
        raiseAttackPoints(playerLevel * 4);
        raiseDefensePoints(playerLevel);
    }

    /**
     * This method describes the process of a game tick with regard to the base player
     * @param a the action taken by the player
     * @return the action taken by the player
     */
    public Action onGameTick(Action a) {
        int tempLevel = playerLevel;
        String prevDesc = description();
        while(experience >= playerLevel * experiencePerLevel)
            onLevelUp();
        if (tempLevel < playerLevel) {
            messageCallback.send("\n\n" + getName() + " has reached level " + playerLevel + ".");
            messageCallback.send("Previous stats were: \n" + prevDesc + "\nAnd now they are: \n" + description());

        }
        return a;
    }

    /**
     * This method is practically redundant and is for making sure each sub-class will override it
     * and implement it differently according to their abilities.
     */
    @Override
    public void castAbility(List<? extends Unit> enemies) {}

    /**
     * This method describes the death of the player
     * @param killer the enemy that killed the player
     */
    @Override
    public void death(Unit killer) {
        notifyDeathObservers(killer,getPos());
    }

    @Override
    public int getExperienceValue() {
        return experience;
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

    /**
     * This method describes a killing process of a unit
     * @param e the unit that the player killed
     */
    public void killed(Unit e) {
        e.death(this);
        addExp(e.getExperienceValue());
        messageCallback.send(getName() + " has received " + e.getExperienceValue() + " experience value.");
    }
    //Visitor Pattern
    @Override
    public boolean visit(Player p) {
        return false;
    }
    @Override
    public boolean visit(Enemy e) {
        if (dealDamage(e))
            killed(e);
        return false;
    }
    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }

    //Observer Pattern
    @Override
    public void addDeathObserver(DeathObserver o) {
        deathObservers.add(o);
    }
    @Override
    public void addWinObserver(WinObserver o) {
        winObservers.add(o);
    }
    @Override
    public void notifyDeathObservers(Unit killer, Position DeathPos) {
        for (DeathObserver o : deathObservers)
            o.onPlayerEvent(killer);
    }
    @Override
    public void notifyWinObservers(boolean endGame) {
        for (WinObserver o : winObservers)
            o.onWinEvent(endGame);
    }

    /**
     * This method describes the player
     * @return String form description
     */
    @Override
    public String description() {
        return super.description() +
                "Experience: " + experience + "/" + experiencePerLevel*playerLevel + "\t" +
                "Level: " + playerLevel + "\t";
    }

}
