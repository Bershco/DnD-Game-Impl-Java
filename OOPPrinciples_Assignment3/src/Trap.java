public class Trap extends Enemy{
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
}
