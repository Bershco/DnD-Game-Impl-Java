package Backend;
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
        alterHealthPoolBy(playerLevel * 5);
        raiseAttackPoints(playerLevel * 2);
        raiseDefensePoints(playerLevel);
    }

    /**
     * This method describes the action and background activity of the warrior regarding their turn
     */
    @Override
    protected Action onGameTick(Action a) {
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
            Random rnd = new Random();
            int index = rnd.nextInt(enemies.size());
            Unit enemy = enemies.get(index);
            enemy.alterHealthPoolBy(-getHealthPool() * 10);
        }
        else {
            throw new IllegalStateException("You can't use that right now!");
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
            "Cooldown: " + remainingCooldown + " out of " + abilityCooldown + "\n";
    }
}
