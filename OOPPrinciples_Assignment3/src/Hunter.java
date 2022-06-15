import java.util.ArrayList;

public class Hunter extends Player {
    private int range;
    private int arrowCount;
    private int tickCount;

    public Hunter(int range){
        this.range = range;
        arrowCount = playerLevel * 10;
        tickCount = 0;
    }

    /**
     * This method describes the process of a level up
     */
    @Override
    public void onLevelUp() {
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
        return super.enoughResources();
    }

    /**
     * This method describes the action and background activity of the hunter regarding their turn
     */
    @Override
    public void onGameTick() {
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
                if(this.range(enemy) > this.range(closestEnemy))
                    closestEnemy = enemy;
            }

            if(attackPoints > closestEnemy.defensePoints)
                closestEnemy.healthAmount -= attackPoints;
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
