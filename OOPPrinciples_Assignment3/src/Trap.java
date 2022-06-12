public class Trap extends Enemy{
    public int visibilityTime;
    public int invisibilityTime;
    public int ticksCount = 0;
    public boolean visible = true;

    private void invert() { visible = !visible;}

    public Trap(int vis, int invis) {
        visibilityTime = vis;
        invisibilityTime = invis;
    }

    public void attackPlayer() {
    }
    @Override
    public void onGameTick() {
        if (ticksCount == visibilityTime) invert();
        if (ticksCount == visibilityTime + invisibilityTime) ticksCount = 0;
        else ticksCount++;
        //if (range(player) < 2) attackPlayer();
    }
}
