package Backend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Rogue extends Player {
    private final int cost;
    private int currentEnergy;
    private final int range = 2;

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
        attackPoints += playerLevel * 3;
    }

    /**
     * This method describes the action and background activity of the rogue regarding their turn
     */
    @Override
    protected void onGameTick(Action a) {
        super.onGameTick(a);

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
    public void castAbility(List<Unit> enemiesOverall) {
        List<Unit> enemies = new LinkedList<>();
        for (Unit enemy : enemiesOverall)
            if (range(enemy) < range)
                enemies.add(enemy);
        if (enoughResources()) {
            currentEnergy -= cost;
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
