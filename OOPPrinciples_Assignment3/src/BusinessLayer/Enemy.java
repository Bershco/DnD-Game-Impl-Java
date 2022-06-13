package BusinessLayer;

public class Enemy extends Unit {
    public int experienceValue;

    Board b = Board.getInstance();

    public void death() {
        b.currentPosition[pos.x][pos.y] = new Empty();
        b.player.addExp(experienceValue);
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
                    case '@' -> dealDamage(b.player);
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
                    case '@' -> dealDamage(b.player);
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
                    case '@' -> dealDamage(b.player);
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
                    case '@' -> dealDamage(b.player);
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

    @Override
    public String description() {
        return super.description() +
            "Experience Value: " + experienceValue + "\n";
    }
}
