package Backend;

public interface DeathObserver {
    void onPlayerEvent(Unit killer);
    void onEnemyEvent(Enemy e);
}
