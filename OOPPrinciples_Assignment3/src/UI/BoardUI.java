package UI;
import Backend.*;

public class BoardUI {
    private final GameMaster gm;

    //Constructor
    public BoardUI(GameMaster gm){
        this.gm = gm;
    }

    /**
     * This method initialises the game using a specific level directory path
     * @param path the levels' directory path
     */
    protected void initialiseGame(String path) {
        try {
            gm.initialiseGame(path);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method prints the current board layout
     */
    protected void printCurrBoard() {
        System.out.println(gm.getCurrentBoard());
    }
}
