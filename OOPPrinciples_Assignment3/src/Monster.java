public class Monster extends Enemy{
    public int visionRange;
    public Tile player; //TODO: remove this
    public void moveProperly() {
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
    public void moveRandomly() {
        int direction = Math.round((float)Math.random()*5) + 1;
        switch (direction) {
            case 1 -> move(Direction.UP);
            case 2 -> move(Direction.LEFT);
            case 3 -> move(Direction.DOWN);
            case 4 -> move(Direction.RIGHT);
            case 5 -> move(Direction.STAND);
        }
    }

    @Override
    public void onGameTick() {
        if (range(player) < visionRange)
            moveProperly();
        else
            moveRandomly();
    }
}