package UI;
import Backend.*;

public class BoardUI {
    private final GameMaster gm;

    public BoardUI(GameMaster gm){
        this.gm = gm;
    }

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
