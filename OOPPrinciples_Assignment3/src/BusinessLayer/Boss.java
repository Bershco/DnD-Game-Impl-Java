package BusinessLayer;

import BusinessLayer.Board;

public class Boss extends Monster implements HeroicUnit {
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
        if (attackPoints > Board.getInstance().player.defensePoints) {
            Board.getInstance().player.healthAmount -= attackPoints;
            if (Board.getInstance().player.healthAmount <= 0)
                Board.getInstance().player.death();
        }
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
