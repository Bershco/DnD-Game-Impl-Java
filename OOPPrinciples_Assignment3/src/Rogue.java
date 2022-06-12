import java.util.ArrayList;

public class Rogue extends Player {
    private int cost;
    private int currentEnergy;

    public Rogue(int cost){
        this.cost = cost;
        currentEnergy = 100;
    }

    @Override
    public void onLevelUp() {
        super.onLevelUp();

        currentEnergy = 100;
        attackPoints += playerLevel * 3;
    }

    @Override
    public void onGameTick() {
        super.onGameTick();

        currentEnergy = Math.min(100, currentEnergy + 10);
    }

    @Override
    protected boolean enoughResources() {
        return currentEnergy >= cost;
    }

    @Override
    public void castAbility() {
        if (enoughResources()) {
            currentEnergy -= cost;
            ArrayList<Enemy> enemies = Board.getInstance().getEnemies(2);
            enemies.forEach(enemy -> {
                if(attackPoints > enemy.defensePoints)
                    enemy.healthAmount -= attackPoints;
            });
        }
        //TODO: provide error message
    }
}
