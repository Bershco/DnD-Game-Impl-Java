package Backend;

import Backend.Tiles.Unit;

public interface Observable {
    public void addDeathObserver(DeathObserver o);
    public void addWinObserver(WinObserver o);
    public void notifyDeathObservers(Unit Killer, Position DeathPos);
    public void notifyWinObservers(boolean endGame);
}
