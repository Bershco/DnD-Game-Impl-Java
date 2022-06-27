package Backend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Mage extends  Player{
    private int manaPool;
    private int currentMana;
    private final int manaCost;
    private int spellPower;
    private final int hitCount;
    private final int abilityRange;

    public Mage(String _name, int _healthPool, int _attackPoints, int _defensePoints, int _manaPool, int _manaCost, int _spellPower, int _hitCount, int _abilityRange, int x, int y) {
        super(_name,_healthPool,_attackPoints,_defensePoints,x,y);
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
    protected Action onGameTick(Action a) {
        currentMana = Math.min(manaPool, currentMana + playerLevel);
        return super.onGameTick(a);
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
    public void castAbility(List<? extends Unit> enemiesOverall) {
        List<Unit> enemies = new LinkedList<>();
        for (Unit enemy : enemiesOverall)
            if (range(enemy)<abilityRange)
                enemies.add(enemy);
        if(enoughResources()) {
            currentMana -= manaCost;
            int hits = 0;
            while (hits < hitCount && enemies.size() > 0) {
                Random rnd = new Random();
                int index = rnd.nextInt(enemies.size());
                Unit enemy = enemies.get(index);
                if(attackPoints > enemy.defensePoints)
                    enemy.healthAmount -= spellPower; //TODO: might be wrong (probably)
                hits++;
            }
        }

        //TODO: provide error message
    }

    @Override
    public String description() {
        return super.description() +
            "Class: Backend.Mage\n" +
            "Mana: " + currentMana + " out of " + manaPool + "\n" +
            "Spell Cost: " + manaCost + "\n" +
            "Spell Power: " + spellPower + "\n" +
            "Ability Range: " + abilityRange + "\n" +
            "Hit Count: " + hitCount + "\n";
    }
}