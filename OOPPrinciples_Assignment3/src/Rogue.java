import java.util.ArrayList;

public class Rogue extends Player {
    private final int cost;
    private int currentEnergy;

    public Rogue(String _name, int _healthPool, int _attackPoints, int _defensePoints, int _cost){
        super(_name,_healthPool,_attackPoints,_defensePoints);
        cost = _cost;
        currentEnergy = 100;
    }

    /**
     * This method describes the process of a level up
     */
    @Override
    protected void onLevelUp() {
        super.onLevelUp();

        currentEnergy = 100;
        attackPoints += playerLevel * 3;
    }

    /**
     * This method describes the action and background activity of the rogue regarding their turn
     */
    @Override
    protected void onGameTick() {
        super.onGameTick();

        currentEnergy = Math.min(100, currentEnergy + 10);
    }

    /**
     * This method dictates if the rogue has enough resources to use his ability
     * @return true if he has enough, false otherwise
     */
    @Override
    protected boolean enoughResources() {
        return currentEnergy >= cost;
    }

    /**
     * This method describes the ability a rogue can cast
     */
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

    @Override
    public String description() {
        return super.description() +
            "Current Energy: " + currentEnergy + " out of 100\n" +
            "Ability Cost: " + cost + "\n" +
            "Ability Range: 2\n";
    }
}
