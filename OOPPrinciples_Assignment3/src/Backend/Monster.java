package Backend;

import java.util.List;

public class Monster extends Enemy{
    protected int visionRange;

    public Monster(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue,int _visionRange,int x, int y) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue,x,y);
        visionRange = _visionRange;
    }

    /**
     * This method describes a proper movement for a monster - is called when the monster is in range of the player
     */
    protected Action moveProperly(Position playerPos) {
        int dx = pos.x - playerPos.x;
        int dy = pos.y - playerPos.y;
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0)
                return move(Action.LEFT);
            else
                return move(Action.RIGHT);
        } else {
            if (dy > 0)
                return move(Action.UP);
            else
                return move(Action.DOWN);
        }
    }

    /**
     * This method describes a random movement for a monster - is called when the monster is NOT in range of the player
     */
    protected Action moveRandomly() {
        return super.randomizeMove();
    }

    /**
     * This method describes the action a monster acts upon after the player's turn
     */
    @Override
    public Action onGameTick(List<Unit> enemiesOfEnemy) {
        Position playerPos = enemiesOfEnemy.get(0).pos;
        if (range(playerPos) < visionRange)
            return moveProperly(playerPos);
        else
            return moveRandomly();
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