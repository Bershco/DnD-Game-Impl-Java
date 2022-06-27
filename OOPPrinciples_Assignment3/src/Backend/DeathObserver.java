package Backend;

public interface DeathObserver {
    public void onPlayerEvent();
    public void onEnemyEvent(Enemy e);
}
