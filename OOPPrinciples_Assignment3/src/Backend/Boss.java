package Backend;

public class Boss extends Monster implements HeroicUnit{
    private final int abilityFrequency;
    private int combatTicks;

    public Boss(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _visionRange, int _experienceValue, int _abilityFrequency) {
        this(_name, _tile, _healthPool, _attackPoints, _defensePoints,_visionRange, _experienceValue, _abilityFrequency,0);
    }
    private Boss(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints,int _visionRange, int _experienceValue, int _abilityFrequency, int _combatTicks) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue,_visionRange);
        visionRange = _visionRange;
        abilityFrequency = _abilityFrequency;
        combatTicks = _combatTicks;
    }

    public int getCombatTicks() {
        return combatTicks;
    }

    /**
     * This method is a followup to a player action if player is in vision range
     */
    @Override
    protected void moveProperly() {
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
        dealDamage(b.getPlayer());
    }

    /**
     * After every action the player performs, the boss will act as the following:
     *  if player is in vision range:
     *      move towards or shoot
     *  else
     *      move randomly and stop being agro
     */
    @Override
    protected void onGameTick() {
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
