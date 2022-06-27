package Backend;

import java.util.List;

public class Monster extends Enemy{
    protected int visionRange;

    public Monster(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue,int _visionRange) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue);
        visionRange = _visionRange;
    }

    /**
     * This method describes a proper movement for a monster - is called when the monster is in range of the player
     */
    protected void moveProperly(Position playerPos) {
        int dx = pos.x - playerPos.x;
        int dy = pos.y - playerPos.y;
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
    public void onGameTick(List<Unit> enemiesOfEnemy) {
        Position playerPos = enemiesOfEnemy.get(0).pos;
        if (range(playerPos) < visionRange)
            moveProperly(playerPos);
        else
            moveRandomly();
    }

    public boolean accept(Monster m) {
        return false;
    }

    public boolean accept(Player p) {
        p.dealDamage(this);
        return false;
    }

    @Override
    public String description() {
        return super.description() +
            "Vision Range: " + visionRange + "\n";
    }
}