public class Trap extends Enemy{
    public int visibilityTime;
    public int invisibilityTime;
    public int ticksCount = 0;
    public boolean visible = true;
    Board b = Board.getInstance();
    Player player = b.getPlayer();
    
    public Trap(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue,int _visionRange, int _visibility, int _invisibility) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue);
        visibilityTime = _visibility;
        invisibilityTime = _invisibility;
    }

    /**
     * This method describes an attack on the player
     */
    public void attackPlayer() {
        dealDamage(player);
    }

    /**
     * This method describes the actions of the trap after a given player action
     */
    @Override
    public void onGameTick() {
        visible = ticksCount < visibilityTime;
        if (ticksCount == (visibilityTime + invisibilityTime))
            ticksCount = 0;
        else ticksCount++;
        if (range(player) < 2) attackPlayer();
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
}
