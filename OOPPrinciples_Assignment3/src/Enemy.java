public class Enemy extends Unit{

    private final int experienceValue;
    protected Board b = Board.getInstance();

    public int getExperienceValue() {
        return experienceValue;
    }


    public Enemy(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints);
        experienceValue = _experienceValue;
    }
    /**
     * This method is part of the combat system of the game, it is used to attack another unit (primarily the player)
     * @param target The unit to attack
     */
    @Override
    protected void dealDamage(Unit target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            target.death();
        }
    }

    /**
     * This method describes a death of an enemy
     */
    protected void death() {
        b.currentPosition[pos.x][pos.y] = new Empty();
        b.player.addExp(experienceValue);
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
    protected void move(Direction d) {
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
                    case '@' -> attackPlayer();
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
                    case '@' -> attackPlayer();
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
                    case '@' -> attackPlayer();
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
                    case '@' -> attackPlayer();
                    default -> randomizeMove();
                }
            }
        }
    }

    protected void attackPlayer() {
        dealDamage(b.player);
    }
    /**
     * This method moves the enemy randomly using a simple Math.random method call
     */
    protected void randomizeMove() {
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
    public String description() {
        return super.description() +
            "Experience Value: " + experienceValue + "\n";
    }
}
