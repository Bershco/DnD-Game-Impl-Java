package UI;
import Backend.Enemy;
import Backend.WinObserver;
import Backend.DeathObserver;
import Backend.gameManager;

public class GameUI implements DeathObserver,WinObserver {
    private final gameManager gm = new gameManager();
    private final BoardUI bui;
    private final PlayerUI pui;
    private boolean win = false;
    private boolean dead = false;

    public GameUI(BoardUI _bui, PlayerUI _pui) {
        bui = _bui;
        pui = _pui;
        gm.addDeathObserver(this);
        gm.addWinObserver(this);
    }
    public void startGame() {
        bui.printCurrBoard();
        pui.printCurrPlayerDesc();
        while (!win && !dead) {

        }
    }

    @Override
    public void onPlayerEvent() {
        dead = true;
    }

    @Override
    public void onEnemyEvent(Enemy e) {
        System.out.println(pui.getPlayerName() + " killed " + e.getName() + " and gained " + e.getExp() + " experience.");

    }

    @Override
    public void onWinEvent() {
        win = true;
    }
}
