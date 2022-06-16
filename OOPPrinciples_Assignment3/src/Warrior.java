import java.util.ArrayList;
import java.util.Random;

public class Warrior extends Player {
    private final int abilityCooldown;
    private int remainingCooldown;

    public Warrior(String _name, int _healthAmount, int _attackPoints, int _defensePoints, int cooldown){
        super(_name,_healthAmount,_attackPoints,_defensePoints);
        abilityCooldown = cooldown;
        remainingCooldown = 0;
    }

    /**
     * This method describes the process of a level up
     */
    @Override
    public void onLevelUp() {
        super.onLevelUp();
        remainingCooldown = 0;
        healthPool += playerLevel * 5;
        attackPoints += playerLevel * 2;
        defensePoints += playerLevel;
    }

    /**
     * This method describes the action and background activity of the warrior regarding their turn
     */
    @Override
    public void onGameTick() {
        super.onGameTick();

        remainingCooldown = Math.max(0, remainingCooldown - 1);
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
    public void castAbility() {
        //TODO: provide error message
        if (enoughResources()) {
            remainingCooldown = abilityCooldown;
            healthAmount = Math.min(healthPool, healthAmount + defensePoints * 10);
            ArrayList<Enemy> closeEnemies = Board.getInstance().getEnemies(3);
            Random rnd = new Random();
            int index = rnd.nextInt(closeEnemies.size());
            Enemy enemy = closeEnemies.get(index);
            enemy.healthAmount -= healthPool * 10;
        }

    }

    @Override
    public String description() {
        return super.description() +
            "Ability Range: 3\n" +
            "Cooldown: " + remainingCooldown + " out of " + abilityCooldown + "\n";
    }
}
