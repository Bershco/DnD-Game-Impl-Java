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

    @Override
    public void onLevelUp() {
        super.onLevelUp();

        arrowCount += playerLevel * 10;
        attackPoints += playerLevel * 2;
        defensePoints += playerLevel;
    }

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

    @Override
    protected boolean enoughResources() {
        return super.enoughResources();
    }

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
