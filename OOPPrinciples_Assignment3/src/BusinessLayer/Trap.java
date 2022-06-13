package BusinessLayer;

import BusinessLayer.Board;
import BusinessLayer.Enemy;

public class Trap extends Enemy {
    public int visibilityTime;
    public int invisibilityTime;
    public int ticksCount = 0;
    public boolean visible = true;

    public Trap(int vis, int invis) {
        visibilityTime = vis;
        invisibilityTime = invis;
    }

    public void attackPlayer() {
    }
    @Override
    public void onGameTick() {
        visible = ticksCount < visibilityTime;
        if (ticksCount == (visibilityTime + invisibilityTime))
            ticksCount = 0;
        else ticksCount++;

        if (range(Board.getInstance().player) < 2) {
            dealDamage(Board.getInstance().player);
        }
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
