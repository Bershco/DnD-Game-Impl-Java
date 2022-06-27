package Backend;

public interface Observable {
    public void addDeathObserver(DeathObserver o);
    public void addWinObserver(WinObserver o);
    public void notifyDeathObservers();
    public void notifyWinObservers();
}
