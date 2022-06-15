public class Boss extends Monster implements HeroicUnit{
    public int abilityFrequency;
    public int combatTicks;
    private final Player player = b.getPlayer();

    public Boss(int _experienceValue,int _visionRange, int _abilityFrequency, int _combatTicks) {
        super(_experienceValue);
        visionRange = _visionRange;
        abilityFrequency = _abilityFrequency;
        combatTicks = _combatTicks;
    }

    /**
     * This method is a followup to a player action if player is in vision range
     */
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

    /**
     * This method is primarily for readability, simply called for only from moveProperly,
     * and only calls for dealDamage, because that's the only functionality needed.
     *** Could later be improved or simply changed without touching moveProperly.
     */
    @Override
    public void castAbility() {
        dealDamage(player);
        /**
        if (attackPoints > Board.getInstance().player.defensePoints) {
            Board.getInstance().player.healthAmount -= attackPoints;
            if (Board.getInstance().player.healthAmount <= 0)
                Board.getInstance().player.death();
        }
        */
    }

    /**
     * After every action the player performs, the boss will act as the following:
     *  if player is in vision range:
     *      move towards or shoot
     *  else
     *      move randomly and stop being agro
     */
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
