package Backend;

import Backend.Tiles.EnemyTiles.Enemy;
import Backend.Tiles.Unit;

public interface DeathObserver {
    void onPlayerEvent(Unit killer);
    void onEnemyEvent(Enemy e);
}
