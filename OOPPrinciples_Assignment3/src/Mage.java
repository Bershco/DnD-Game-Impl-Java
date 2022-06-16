import java.util.ArrayList;
import java.util.Random;

public class Mage extends  Player{
    private int manaPool;
    private int currentMana;
    private final int manaCost;
    private int spellPower;
    private final int hitCount;
    private final int abilityRange;

    public Mage(String _name, int _healthPool, int _attackPoints, int _defensePoints, int _manaPool, int _manaCost, int _spellPower, int _hitCount, int _abilityRange) {
        super(_name,_healthPool,_attackPoints,_defensePoints);
        manaPool = _manaPool;
        currentMana = manaPool / 4;
        manaCost = _manaCost;
        spellPower = _spellPower;
        hitCount = _hitCount;
        abilityRange = _abilityRange;
    }

    /**
     * This method describes the process of a level up
     */
    @Override
    protected void onLevelUp() {
        super.onLevelUp();

        manaPool += playerLevel * 25;
        currentMana = Math.min(manaPool, currentMana + manaPool / 4);
        spellPower += playerLevel * 10;
    }

    /**
     * This method describes the action and background activity of the mage regarding their turn
     */
    @Override
    protected void onGameTick() {
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
