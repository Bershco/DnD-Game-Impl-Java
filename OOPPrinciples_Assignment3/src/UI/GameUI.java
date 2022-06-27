package UI;
import Backend.*;
import java.util.Scanner;

public class GameUI implements DeathObserver,WinObserver {
    private final GameMaster gm = new GameMaster();
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
        Scanner scanner = new Scanner(System.in);
        String availableChars = "wasdeq";
        while (!win && !dead) {
            String input = scanner.nextLine().toLowerCase();
            while (!availableChars.contains(input) || input.length() != 1)
                input = scanner.nextLine().toLowerCase();
            switch (input) {
                case "w" -> {
                    gm.onGameTick(Action.UP);
                }
                case "a" -> {
                    gm.onGameTick(Action.LEFT);
                }
                case "s" -> {
                    gm.onGameTick(Action.DOWN);
                }
                case "d" -> {
                    gm.onGameTick(Action.RIGHT);
                }
                case "e" -> {
                    System.out.println(gm.onGameTick(Action.ABILITYCAST));
                }
                case "q" -> {
                    gm.onGameTick(Action.STAND);
                }
            }
            bui.printCurrBoard();
            pui.printCurrPlayerDesc();
        }
        bui.printCurrBoard();
    }

    @Override
    public void onPlayerEvent() {
        dead = true;
        System.out.println("Game over.....................?"); //TODO: maybe have a lose board as well as the win board
    }

    @Override
    public void onEnemyEvent(Enemy e) {
        System.out.println(pui.getPlayerName() + " killed " + e.getName() + " and gained " + e.getExp() + " experience.");

    }

    @Override
    public void onWinEvent(boolean endGame) {
        win = endGame;
        System.out.println((!endGame) ? "Level completed, congratulations, here's the next one:" : "Game over.....................?");
    }
}
