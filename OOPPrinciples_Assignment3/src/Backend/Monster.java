package Backend;

import java.util.List;

public class Monster extends Enemy{
    protected int visionRange;

    //Constructor
    public Monster(String _name, char _tile, int _healthPool, int _attackPoints, int _defensePoints, int _experienceValue,int _visionRange,int x, int y) {
        super(_name,_tile,_healthPool,_attackPoints,_defensePoints,_experienceValue,x,y);
        visionRange = _visionRange;
    }

    /**
     * This method describes a proper movement for a monster - is called when the monster is in range of the player
     */
    protected Action movementBasedOnFunction(Position playerPos) {
        int dx = getX() - playerPos.getX();
        int dy = getY() - playerPos.getY();
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0)
                return Action.LEFT;
            else
                return Action.RIGHT;
        } else {
            if (dy > 0)
                return Action.UP;
            else
                return Action.DOWN;
        }
    }

    /**
     * This method describes the action a monster acts upon after the player's turn
     */
    @Override
    protected Action onGameTick(List<Unit> enemiesOfEnemy) {
        Position playerPos = enemiesOfEnemy.get(0).getPos();
        if (range(playerPos) < visionRange)
            return movementBasedOnFunction(playerPos);
        else
            return randomizeMove();
    }

    /**
     * This method describes the monster
     * @return String form description
     */
    @Override
    public String description() {
        return super.description() +
            "Vision Range: " + visionRange + "\n";
    }
}