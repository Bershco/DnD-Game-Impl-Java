public class Enemy extends Unit{
    public int experienceValue;

    Board b = Board.getInstance();
    @Override
    public void dealDamage(Unit target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            target.death();
        }
    }

    public void death() {
        b.currentPosition[pos.x][pos.y] = new Empty();
        b.removeEnemy(this);
    }
    
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
