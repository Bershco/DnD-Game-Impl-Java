package UI;
import Backend.*;
import java.util.Scanner;

public class GameUI implements DeathObserver,WinObserver {
    private final GameMaster gm;
    private final BoardUI bui;
    private final PlayerUI pui;
    private boolean win = false;
    private boolean dead = false;

    //Constructor
    public GameUI(String levelPath) {
        gm = new GameMaster(this::print);
        bui = new BoardUI(gm);
        pui = new PlayerUI(gm);
        bui.initialiseGame(levelPath);
        pui.initialisePlayer();
        gm.addDeathObserver(this);
        gm.addWinObserver(this);
    }

    /**
     * This method starts the game
     */
    public void startGame() {
        bui.printCurrBoard();
        pui.printCurrPlayerDesc();
        Scanner scanner = new Scanner(System.in);
        String availableChars = "wasdeq";
        while (!win && !dead) {
            String input = scanner.nextLine().toLowerCase();
            while (!availableChars.contains(input) || input.length() != 1)
                input = scanner.nextLine().toLowerCase();
            pseudoClearScreen();
            switch (input) {
                case "w" -> gm.onGameTick(Action.UP);
                case "a" -> gm.onGameTick(Action.LEFT);
                case "s" -> gm.onGameTick(Action.DOWN);
                case "d" -> gm.onGameTick(Action.RIGHT);
                case "e" -> gm.onGameTick(Action.ABILITYCAST);
                case "q" -> gm.onGameTick(Action.STAND);
            }
            bui.printCurrBoard();
            pui.printCurrPlayerDesc();
        }
        if (win) gm.loadWin();
        else gm.loadLose();
        bui.printCurrBoard();
    }

    /**
     * This method describes what happens on the event of a death of a player
     * @param killer the unit that killed the player
     */
    @Override
    public void onPlayerEvent(Unit killer) {
        dead = true;
        System.out.println("\n\nGame over.....................?\n");
    }

    /**
     * This method describes what happens on the event of a death of an enemy
     * @param e the dead enemy
     */
    @Override
    public void onEnemyEvent(Enemy e) {
        System.out.println(pui.getPlayerName() + " killed " + e.getName() + " and gained " + e.getExperienceValue() + " experience.");

    }

    /**
     * This method describes what happens when the player finishes a level
     * @param endGame a boolean variable determining whether the player finished a level or the entire game
     */
    @Override
    public void onWinEvent(boolean endGame) {
        win = endGame;
        System.out.println((!endGame) ? "Level completed, congratulations, here's the next one:" : "Game over.....................?");
    }

    /**
     * This method prints out the message it is received, used by messageCallbacks
     * @param message the message to be print
     */
    public void print(String message){
        System.out.println(message);
    }

    /**
     * This method simply prints a LOT of newline characters in order to clear the screen after each action
     */
    public void pseudoClearScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
}
