package Backend;

import java.util.LinkedList;
import java.util.List;

public class Rogue extends Player {
    private final int cost;
    private int currentEnergy;
    private final int range = 2;

    //Constructor
    public Rogue(String _name, int _healthPool, int _attackPoints, int _defensePoints, int _cost, int x, int y){
        super(_name,_healthPool,_attackPoints,_defensePoints,x,y);
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
        raiseAttackPoints(playerLevel * 3);
    }

    /**
     * This method describes the action and background activity of the rogue regarding their turn
     */
    @Override
    protected Action onGameTick(Action a) {
        currentEnergy = Math.min(100, currentEnergy + 10);
        return super.onGameTick(a);
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
    public void castAbility(List<? extends Unit> enemiesOverall) {
        List<Unit> enemies = new LinkedList<>();
        for (Unit enemy : enemiesOverall)
            if (range(enemy) < range)
                enemies.add(enemy);
        if (enoughResources()) {
            currentEnergy -= cost;
            enemies.forEach(enemy -> {
                if(getAttackPoints() > enemy.getDefensePoints())
                    enemy.alterHealthPoolBy(getAttackPoints()); //TODO: this isn't true
            });
        }
        else {
            throw new IllegalStateException("You can't use that right now!");
        }
    }

    /**
     * This method describes the rogue player
     * @return String form description
     */
    @Override
    public String description() {
        return super.description() +
            "Current Energy: " + currentEnergy + " out of 100\n" +
            "Ability Cost: " + cost + "\n" +
            "Ability Range: 2\n";
    }
}
