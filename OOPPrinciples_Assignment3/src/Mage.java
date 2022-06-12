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
            while (hits < hitCount ) { //TODO: add living enemy exists within range clause
                //TODO: damage an enemy within the ability range (it will try to defend itself)
                hits++;
            }
        }

        //TODO: provide error message
    }
}
