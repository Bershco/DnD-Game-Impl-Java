public class Monster extends Enemy{
    public int visionRange;
    private final Tile player = b.getPlayer(); //TODO: remove this

    public Monster(int exp) {
        setExperienceValue(exp);
    }

    /**
     * This method describes a proper movement for a monster - is called when the monster is in range of the player
     */
    protected void moveProperly() {
        int dx = pos.x - player.pos.x;
        int dy = pos.y - player.pos.y;
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0)
                move(Direction.LEFT);
            else
                move(Direction.RIGHT);
        } else {
            if (dy > 0)
                move(Direction.UP);
            else
                move(Direction.DOWN);
        }
    }

    /**
     * This method describes a random movement for a monster - is called when the monster is NOT in range of the player
     */
    protected void moveRandomly() {
        super.randomizeMove();
    }

    /**
     * This method describes the action a monster acts upon after the player's turn
     */
    @Override
    public void onGameTick() {
        if (range(player) < visionRange)
            moveProperly();
        else
            moveRandomly();
    }
}