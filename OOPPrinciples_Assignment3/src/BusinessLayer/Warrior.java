package BusinessLayer;

import BusinessLayer.Board;
import BusinessLayer.Enemy;
import BusinessLayer.Player;

import java.util.ArrayList;
import java.util.Random;

public class Warrior extends Player {
    private int abilityCooldown;
    private int remainingCooldown;

    public Warrior(int cooldown){
        abilityCooldown = cooldown;
        remainingCooldown = 0;
    }

    @Override
    public void onLevelUp() {
        super.onLevelUp();
        remainingCooldown = 0;
        healthPool += playerLevel * 5;
        attackPoints += playerLevel * 2;
        defensePoints += playerLevel;
    }

    @Override
    public void onGameTick() {
        super.onGameTick();

        remainingCooldown = Math.max(0, remainingCooldown - 1);
    }

    @Override
    protected boolean enoughResources() {
        return remainingCooldown == 0;
    }

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
            return;
        }

    }

    @Override
    public String description() {
        return super.description() +
            "Ability Range: 3\n" +
            "Cooldown: " + remainingCooldown + " out of " + abilityCooldown + "\n";
    }
}
