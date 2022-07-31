package Backend;
import java.util.LinkedList;
import java.util.List;

public class Hunter extends Player {
    private final int range;
    private int arrowCount;
    private int tickCount;

    //Constructor
    public Hunter(String _name, int _healthPool, int _attackPoints, int _defensePoints, int _range, int x, int y){
        super(_name,_healthPool,_attackPoints,_defensePoints,x,y);
        range = _range;
        arrowCount = playerLevel * 10;
        tickCount = 0;
    }

    /**
     * This method describes the process of a level up
     */
    @Override
    protected void onLevelUp() {
        super.onLevelUp();
        arrowCount += playerLevel * 10;
        raiseAttackPoints(playerLevel * 2);
        raiseDefensePoints(playerLevel);
    }

    /**
     * This method dictates if the hunter has enough resources to use his ability
     * @return true if he has enough, false otherwise
     */
    @Override
    protected boolean enoughResources() {
        return arrowCount != 0;
    }

    /**
     * This method describes the action and background activity of the hunter regarding their turn
     */
    @Override
    protected Action onGameTick(Action a) {
        if(tickCount == 10) {
            arrowCount += playerLevel;
            tickCount = 0;
        }
        else
            tickCount++;
        return super.onGameTick(a);
    }

    /**
     * This method describes the ability a hunter can cast
     */
    @Override
    public void castAbility(List<? extends Unit> enemiesOverall) {
        List<Unit> enemiesInRange = new LinkedList<>();
        for (Unit enemy : enemiesOverall)
            if (range(enemy)<=range)
                enemiesInRange.add(enemy);
        if(enoughResources() && !enemiesInRange.isEmpty()) {
            arrowCount--;
            Unit closestEnemy = enemiesInRange.get(0);
            for (Unit enemy : enemiesInRange) {
                if(range(enemy) < range(closestEnemy))
                    closestEnemy = enemy;
            }
            messageCallback.send(getName() + " used the special ability against " + closestEnemy.getName());
            dealDamage(closestEnemy);
        }
        else {
            throw new IllegalStateException("You can't use your special ability right now as you dont have enough resources or it is still in cooldown");
        }
    }

    /**
     * This method describes the hunter player
     * @return String form description
     */
    @Override
    public String description() {
        return super.description() +
            "Attack Range: " + range + "\n" +
            "Remaining Arrows: " + arrowCount + "\n" +
            "Countdown Until Arrow: " + (10 - tickCount) + "\n";
    }
}
