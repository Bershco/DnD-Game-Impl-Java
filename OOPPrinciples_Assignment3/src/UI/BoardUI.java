package UI;
import Backend.*;

public class BoardUI {
    private final gameManager gm = new gameManager();

    public void initialiseGame(String path) {
        gm.initialiseGame(path);
    }

    public void printCurrBoard() {
        System.out.println(gm.getCurrentBoard());
    }

}
