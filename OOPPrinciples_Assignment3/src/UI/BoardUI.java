package UI;
import Backend.*;

public class BoardUI {
    private final GameMaster gm = new GameMaster();

    public void initialiseGame(String path) {
        try {
            gm.initialiseGame(path);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printCurrBoard() {
        System.out.println(gm.getCurrentBoard());
    }

}
