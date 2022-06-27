package Backend;

public interface Observable {
    public void addObserver(DeathObserver o);
    public void notifyObservers();
}
