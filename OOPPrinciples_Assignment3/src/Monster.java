public class Monster extends Enemy{
    public int visionRange;
    protected void moveProperly() {
        int dx = pos.x - Board.getInstance().player.pos.x;
        int dy = pos.y - Board.getInstance().player.pos.y;
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
    protected void moveRandomly() {
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
        if (range(Board.getInstance().player) < visionRange)
            moveProperly();
        else
            moveRandomly();
    }
}