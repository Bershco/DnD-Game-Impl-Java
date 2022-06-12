public class Boss extends Monster implements HeroicUnit{
    public int visionRange;
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
        if (range(player) < visionRange)
            moveProperly();
        else
            combatTicks = 0;
            moveRandomly();
    }
}
