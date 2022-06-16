import java.util.ArrayList;

public class Hunter extends Player {
    private final int range;
    private int arrowCount;
    private int tickCount;

    public Hunter(String _name, int _healthPool, int _attackPoints, int _defensePoints, int _range){
        super(_name,_healthPool,_attackPoints,_defensePoints);
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
        attackPoints += playerLevel * 2;
        defensePoints += playerLevel;
    }

    /**
     * This method dictates if the hunter has enough resources to use his ability
     * @return true if he has enough, false otherwise
     */
    @Override
    protected boolean enoughResources() {
        ArrayList<Enemy> enemies = Board.getInstance().getEnemies(range);
        return (arrowCount != 0) && (!enemies.isEmpty());
    }

    /**
     * This method describes the action and background activity of the hunter regarding their turn
     */
    @Override
    protected void onGameTick() {
        super.onGameTick();

        if(tickCount == 10) {
            arrowCount += playerLevel;
            tickCount = 0;
        }
        else
            tickCount++;
    }

    /**
     * This method describes the ability a hunter can cast
     */
    @Override
    public void castAbility() {
        if(enoughResources()) {
            arrowCount--;
            ArrayList<Enemy> enemies = Board.getInstance().getEnemies(range);
            Enemy closestEnemy = enemies.get(0);
            for (Enemy enemy : enemies) {
                if(range(enemy) < range(closestEnemy))
                    closestEnemy = enemy;
            }

            dealDamage(closestEnemy);
        }
        //TODO: provide error message
    }

    @Override
    public String description() {
        return super.description() +
            "Attack Range: " + range + "\n" +
            "Remaining Arrows: " + arrowCount + "\n" +
            "Countdown Until Arrow: " + (10 - tickCount) + "\n";
    }
}
