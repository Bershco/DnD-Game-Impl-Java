import java.util.ArrayList;
import java.util.Random;

public class Mage extends  Player{
    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int spellPower;
    private int hitCount;
    private int abilityRange;

    public Mage(int manaPool, int manaCost, int spellPower, int hitCount, int abilityRange) {
        this.manaPool = manaPool;
        currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitCount = hitCount;
        this.abilityRange = abilityRange;
    }

    /**
     * This method describes the process of a level up
     */
    @Override
    public void onLevelUp() {
        super.onLevelUp();

        manaPool += playerLevel * 25;
        currentMana = Math.min(manaPool, currentMana + manaPool / 4);
        spellPower += playerLevel * 10;
    }

    /**
     * This method describes the action and background activity of the mage regarding their turn
     */
    @Override
    public void onGameTick() {
        super.onGameTick();

        currentMana = Math.min(manaPool, currentMana + playerLevel);
    }

    /**
     * This method dictates if the mage has enough resources to use his ability
     * @return true if he has enough, false otherwise
     */
    @Override
    protected boolean enoughResources() {
        return currentMana >= manaCost;
    }

    /**
     * This method describes the ability a hunter can cast
     */
    @Override
    public void castAbility() {
        if(enoughResources()) {
            currentMana -= manaCost;
            int hits = 0;
            ArrayList<Enemy> enemies = Board.getInstance().getEnemies(abilityRange);
            while (hits < hitCount && enemies.size() > 0) {
                Random rnd = new Random();
                int index = rnd.nextInt(enemies.size());
                Enemy enemy = enemies.get(index);
                if(attackPoints > enemy.defensePoints)
                    enemy.healthAmount -= spellPower;
                hits++;
            }
        }

        //TODO: provide error message
    }
}
