package Backend;

import java.util.List;

public class Trap extends Enemy{
    private final int visibilityTime;
    private final int invisibilityTime;
    private int ticksCount = 0;
    private boolean visible = true;

    public Trap(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue, int _visibility, int _invisibility, int x, int y) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue,x,y);
        visibilityTime = _visibility;
        invisibilityTime = _invisibility;
    }

    /**
     * This method describes the actions of the trap after a given player action
     */
    @Override
    public Action onGameTick(List<Unit> enemiesOfEnemy) {
        Unit player = enemiesOfEnemy.get(0);
        visible = ticksCount < visibilityTime;
        if (ticksCount == (visibilityTime + invisibilityTime))
            ticksCount = 0;
        else
            ticksCount++;
        if (range(player.pos) < 2)
            dealDamage(player);
        return Action.STAND;
    }

    @Override
    public String description() {
        String output = super.description() +
            "Visible: " + visible + "\n" +
            "Visibility Time: " + visibilityTime + "\n" +
            "Invisibility Time: " + invisibilityTime + "\n";
        if (visible)
            output += "Time Until Invisibility: " + (visibilityTime - ticksCount) + "\n";
        else
            output += "Time Until Visibility: " + (visibilityTime + invisibilityTime - ticksCount) + "\n";
        return output;
    }

    @Override
    public String toString() {
        return (ticksCount < visibilityTime) ? super.toString() : ".";
    }
}
