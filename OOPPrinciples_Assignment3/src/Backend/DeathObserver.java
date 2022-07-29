package Backend;

public interface DeathObserver {
    public void onPlayerEvent(Unit killer);
    public void onEnemyEvent(Enemy e);
}
