public class Enemy extends Unit{

    private int experienceValue;
    Board b = Board.getInstance();

    public int getExperienceValue() {
        return experienceValue;
    }

    protected void setExperienceValue(int exp) {
        experienceValue = exp;
    }

    /**
     * This method is part of the combat system of the game, it is used to attack another unit (primarily the player)
     * @param target The unit to attack
     */
    @Override
    public void dealDamage(Unit target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            target.death();
        }
    }

    /**
     * This method describes a death of an enemy
     */
    public void death() {
        b.currentPosition[pos.x][pos.y] = new Empty();
        b.removeEnemy(this);
    }

    /**
     * This method attempts movement of the enemy to a certain direction, followed by the following options:
     *      1. If the attempted tile is empty, simply move
     *      2. If the attempted tile is the player, attempt fighting sequence
     *      3. If the attempted tile is a wall, just randomly move to a different direction
     * @param d the attempted direction to move towards
     */
    @Override
    public void move(Direction d) {
        Board b = Board.getInstance();
        switch (d) {
            case UP -> {
                switch (b.currentPosition[pos.x][pos.y+1].tile) {
                    case '.' -> {
                        Tile middleman = b.currentPosition[pos.x][pos.y+1];
                        b.currentPosition[pos.x][pos.y+1] = this;
                        b.currentPosition[pos.x][pos.y] = middleman;
                        pos.y++;
                    }
                    //case '@' -> return; //TODO: Implement attacking sequence
                    default -> randomizeMove();
                }
            }
            case DOWN -> {
                switch (b.currentPosition[pos.x][pos.y-1].tile) {
                    case '.' -> {
                        Tile middleman = b.currentPosition[pos.x][pos.y-1];
                        b.currentPosition[pos.x][pos.y-1] = this;
                        b.currentPosition[pos.x][pos.y] = middleman;
                        pos.y--;
                    }
                    //case '@' -> return; //TODO: Implement attacking sequence
                    default -> randomizeMove();
                }
            }
            case LEFT -> {
                switch (b.currentPosition[pos.x+1][pos.y].tile) {
                    case '.' -> {
                        Tile middleman = b.currentPosition[pos.x+1][pos.y];
                        b.currentPosition[pos.x+1][pos.y] = this;
                        b.currentPosition[pos.x][pos.y] = middleman;
                        pos.x++;
                    }
                    //case '@' -> return; //TODO: Implement attacking sequence
                    default -> randomizeMove();
                }
            }
            case RIGHT -> {
                switch (b.currentPosition[pos.x-1][pos.y].tile) {
                    case '.' -> {
                        Tile middleman = b.currentPosition[pos.x-1][pos.y];
                        b.currentPosition[pos.x-1][pos.y] = this;
                        b.currentPosition[pos.x][pos.y] = middleman;
                        pos.x--;
                    }
                    //case '@' -> return; //TODO: Implement attacking sequence
                    default -> randomizeMove();
                }
            }
        }
    }

    /**
     * This method moves the enemy randomly using a simple Math.random method call
     */
    public void randomizeMove() {
        int direction = Math.round((float)Math.random()*5) + 1;
        switch (direction) {
            case 1 -> move(Direction.UP);
            case 2 -> move(Direction.LEFT);
            case 3 -> move(Direction.DOWN);
            case 4 -> move(Direction.RIGHT);
            case 5 -> move(Direction.STAND);
        }

    }
}
