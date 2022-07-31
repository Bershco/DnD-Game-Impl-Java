package Backend.Tiles.PlayerTiles;
import Backend.Action;
import Backend.Tiles.Unit;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Warrior extends Player {
    private final int abilityCooldown;
    private int remainingCooldown;
    private final int range = 3;

    //Constructor
    public Warrior(String _name, int _healthPool, int _attackPoints, int _defensePoints, int cooldown, int x, int y){
        super(_name,_healthPool,_attackPoints,_defensePoints,x,y);
        abilityCooldown = cooldown;
        remainingCooldown = 0;
    }

    /**
     * This method describes the process of a level up
     */
    @Override
    protected void onLevelUp() {
        super.onLevelUp();
        remainingCooldown = 0;
        raiseHealthPoolBy(playerLevel * 5);
        raiseAttackPoints(playerLevel * 2);
        raiseDefensePoints(playerLevel);
    }

    /**
     * This method describes the action and background activity of the warrior regarding their turn
     */
    @Override
    public Action onGameTick(Action a) {
        remainingCooldown = Math.max(0, remainingCooldown - 1);
        return super.onGameTick(a);
    }

    /**
     * This method dictates if the warrior has enough resources to use his ability
     * @return true if he has enough, false otherwise
     */
    @Override
    protected boolean enoughResources() {
        return remainingCooldown == 0;
    }

    /**
     * This method describes the ability a warrior can cast
     */
    @Override
    public void castAbility(List<? extends Unit> enemiesOverall) {
        List<Unit> enemies = new LinkedList<>();
        for (Unit enemy : enemiesOverall)
            if (range(enemy) < range)
                enemies.add(enemy);
        if (enoughResources()) {
            remainingCooldown = abilityCooldown;
            setHealthAmount(Math.min(getHealthPool(), getHealthAmount() + getDefensePoints() * 10));
            messageCallback.send("=================================================\n\t\t You have cast the special ability\n=================================================");
            messageCallback.send(getName() + "'s health is now " + getHealthAmount());
            if (!enemies.isEmpty()) {
                Random rnd = new Random();
                int index = rnd.nextInt(enemies.size());
                Unit enemy = enemies.get(index);
                enemy.alterHealthAmountBy(-getHealthPool()/10);
                /* The requirement states the following:
                 * "Randomly hits one enemy within range < 3 for an amount equals to 10% of the warrior’s health pool"
                 * so we *intentionally* did not use the method dealDamage.
                 */
                if (enemy.getHealthAmount() > 0)
                    messageCallback.send(getName() + " used the special ability against " + enemy.getName() + " and dealt " + (getHealthPool()/10) + " damage.\n" + enemy.getName() + "'s health is now at " + enemy.getHealthAmount());
                else {
                    messageCallback.send(getName() + " used the special ability against " + enemy.getName() + " and dealt " + (getHealthPool()/10) + " damage.\n" + enemy.getName() + " is now dead");
                    killed(enemy);
                }
            }
        }
        else {
            throw new IllegalStateException("You can't use your special ability right now as you dont have enough resources or it is still in cooldown");
        }
    }

    /**
     * This method describes the warrior player
     * @return String form description
     */
    @Override
    public String description() {
        return super.description() +
            "Ability Range: "+range+"\n" +
            "Cooldown: " + remainingCooldown + " turns.\n";
    }
}
