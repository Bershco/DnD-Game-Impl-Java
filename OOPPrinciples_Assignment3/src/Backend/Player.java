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
        messageCallback.send(getName() + " has reached level " + playerLevel); // TODO: Complete
    }

    protected Action onGameTick(Action a) {
        if(experience >= playerLevel * experiencePerLevel)
            onLevelUp();
        return a;
    }

    /**
     * This method is practically redundant and is for making sure each sub-class will override it
     * and implement it differently according to their abilities.
     */
    @Override
    public void castAbility(List<? extends Unit> enemies) {}

    /**
     * This method describes the death of the player - simply calling the method gameOverLose() in our singleton board - in order to finish the game
     * @param killer
     */
    @Override
    protected void death(Unit killer) {
        notifyDeathObservers(killer,pos);
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
    public boolean accept(Unit u) {
        u.dealDamage(this);
        return false;
    }

    @Override
    public boolean visit(Enemy e) {
        if (e.accept(this)) {
            swapSurroundingsWith(e);
            swapPositionsWith(e);
            return true;
        }
        return false;
    }

    @Override
    public boolean visit(Player p) {
        return false;
    }

    @Override
    public String description() {
        return super.description() +
            "Experience: " + experience + "/" + experiencePerLevel*playerLevel + "\t" +
            "Level: " + playerLevel + "\t";
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
    public void notifyDeathObservers(Unit killer, Position DeathPos) {
        for (DeathObserver o : deathObservers)
            o.onPlayerEvent(killer);
    }

    @Override
    public void notifyWinObservers(boolean endGame) {
        for (WinObserver o : winObservers)
            o.onWinEvent(endGame);
    }
}
