public class Trap extends Enemy{
    public int visibilityTime;
    public int invisibilityTime;
    public int ticksCount = 0;
    public boolean visible = true;
    Board b = Board.getInstance();
    Player player = b.getPlayer();

    /**
     * This method is a helper method to invert the trap from invisible to visible and vice versa
     */
    private void invert() { visible = !visible;}
  
    public Trap(int vis, int invis) {
        visibilityTime = vis;
        invisibilityTime = invis;
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

    /**
     * This method describes a death of a trap, which shouldn't happen
     */
    @Override
    public void death() {
        throw new IllegalArgumentException("a trap cannot die, something went wrong");

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
