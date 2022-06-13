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

    @Override
    public void onLevelUp() {
        super.onLevelUp();

        manaPool += playerLevel * 25;
        currentMana = Math.min(manaPool, currentMana + manaPool / 4);
        spellPower += playerLevel * 10;
    }

    @Override
    public void onGameTick() {
        super.onGameTick();

        currentMana = Math.min(manaPool, currentMana + playerLevel);
    }

    @Override
    protected boolean enoughResources() {
        return currentMana >= manaCost;
    }

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

    @Override
    public String description() {
        return super.description() +
            "Class: Mage\n" +
            "Mana: " + currentMana + " out of " + manaPool + "\n" +
            "Spell Cost: " + manaCost + "\n" +
            "Spell Power: " + spellPower + "\n" +
            "Ability Range: " + abilityRange + "\n" +
            "Hit Count: " + hitCount + "\n";
    }
}
