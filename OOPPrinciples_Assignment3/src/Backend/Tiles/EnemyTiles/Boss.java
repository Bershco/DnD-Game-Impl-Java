package Backend.Tiles.EnemyTiles;

import Backend.Action;
import Backend.HeroicUnit;
import Backend.Tiles.Unit;

import java.util.List;

public class Boss extends Monster implements HeroicUnit {
    private final int abilityFrequency;
    private int combatTicks;

    //Constructors
    public Boss(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _visionRange, int _experienceValue, int _abilityFrequency, int x, int y) {
        this(_name, _tile, _healthPool, _attackPoints, _defensePoints,_visionRange, _experienceValue, _abilityFrequency,0,x,y);
    }
    private Boss(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints,int _visionRange, int _experienceValue, int _abilityFrequency, int _combatTicks, int x, int y) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue,_visionRange,x,y);
        visionRange = _visionRange;
        abilityFrequency = _abilityFrequency;
        combatTicks = _combatTicks;
    }
    /**
     * This method is a followup to a player action if player is in vision range
     */
    protected Action moveProperly(List<Unit> enemiesOfEnemy) {
        if (combatTicks >= abilityFrequency) {
            combatTicks = 0;
            castAbility(enemiesOfEnemy);
            return Action.STAND;
        }
        else {
            combatTicks++;
            return super.movementBasedOnFunction(enemiesOfEnemy.get(0).getPos());
        }
    }
    /**
     * This method is primarily for readability, simply called for only from moveProperly,
     * and only calls for dealDamage, because that's the only functionality needed.
     *** Could later be improved or simply changed without touching moveProperly.
     */
    @Override
    public void castAbility(List<? extends Unit> enemies) {
        for (Unit enemy : enemies) {
            dealDamage(enemy);
        }
    }

    /**
     * After every action the player performs, the boss will act as the following:
     *  if player is in vision range:
     *      move towards or shoot
     *  else
     *      move randomly and stop being agro
     */
    @Override
    public Action onGameTick(List<Unit> enemiesOfEnemy) {
        if (range(enemiesOfEnemy.get(0).getPos()) < visionRange)
            return moveProperly(enemiesOfEnemy);
        else {
            combatTicks = 0;
            return randomizeMove();
        }
    }

    @Override
    public String description() {
        return super.description() +
            "Cooldown: " + (abilityFrequency - combatTicks) + " out of " + abilityFrequency + "\n";
    }
}
