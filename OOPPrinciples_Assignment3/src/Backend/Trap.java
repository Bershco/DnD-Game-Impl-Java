package Backend;

public class Trap extends Enemy{
    private final int visibilityTime;
    private final int invisibilityTime;
    private int ticksCount = 0;
    private boolean visible = true;

    public Trap(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue,int _visionRange, int _visibility, int _invisibility) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue);
        visibilityTime = _visibility;
        invisibilityTime = _invisibility;
    }

    /**
     * This method describes the actions of the trap after a given player action
     */
    @Override
    protected void onGameTick() {
        visible = ticksCount < visibilityTime;
        if (ticksCount == (visibilityTime + invisibilityTime))
            ticksCount = 0;
        else ticksCount++;
        if (range(Board.getInstance().getPlayer()) < 2) attackPlayer();
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
