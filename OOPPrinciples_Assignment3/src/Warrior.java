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
            return;
        }

    }
}
