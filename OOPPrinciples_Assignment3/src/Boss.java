public class Boss extends Monster implements HeroicUnit{
    public int abilityFrequency;
    public int combatTicks;

    @Override
    public void moveProperly() {
        if (combatTicks == abilityFrequency) {
            combatTicks = 0;
            castAbility();
        }
        else {
            combatTicks++;
            super.moveProperly();
        }
    }
    @Override
    public void castAbility() {
        //TODO implement castAbility()
    }
    @Override
    public void onGameTick() {
        if (range(Board.getInstance().player) < visionRange)
            moveProperly();
        else {
            combatTicks = 0;
            moveRandomly();
        }
    }

    @Override
    public String description() {
        return super.description() +
            "Cooldown: " + (abilityFrequency - combatTicks) + " out of " + abilityFrequency + "\n";
    }
}
