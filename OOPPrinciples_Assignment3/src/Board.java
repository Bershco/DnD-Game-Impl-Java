import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Board instance;
    protected Tile[][] currentPosition;
    private ArrayList<Enemy> allEnemies;
    private ArrayList<Wall> allWalls;
    protected Player player;

    /**
     * The board is a singleton.
     */
    private Board(){

    }
    

    /**
     * This method returns an instance of the player being played by the user
     * @return an instance of the player
     */
    protected Player getPlayer() {
        return player;
    }

    /**
     * This method returns an instance of the board as it is a singleton and shouldn't occur more than once
     * @return an instance of the current board
     */
    protected static Board getInstance() {
        if(instance == null)
            instance = new Board();
        return instance;
    }


    /**
     * This method is a helper method to castAbility() in types of Players that need a list of enemies in a valid range
     * @param range the valid range
     * @return a list of enemies in given range
     */
    protected ArrayList<Enemy> getEnemies(int range) {
        ArrayList<Enemy> closeEnemies = new ArrayList<>();
        allEnemies.forEach(enemy -> {
            if(player.range(enemy) < range)
                closeEnemies.add(enemy);
        });
        return closeEnemies;
    }

    /**
     * This method removes an enemy from the enemy list so that it won't exist in the game anymore
     *      This method is only called from death() of enemy
     * @param e the enemy to be removed
     */
    protected void removeEnemy(Enemy e) {
        allEnemies.remove(e);
    }

    /**
     * This method acts after a player action - calling all enemies to play their turn
     */
    protected void onGameTick() {
        for (Enemy e : allEnemies) {
            e.onGameTick();
        }
    }

    /**
     * This method describes the turn of events when you lose the game
     */
    protected void gameOverLose() {
        Tile grave = new Grave(player.pos);
        currentPosition[grave.pos.x][grave.pos.y] = grave;
        //TODO: provide error message
    }

    protected void gameOverWin() {
        throw new UnsupportedOperationException("Not implemented yet");
        //TODO: Implement gameOverWin()
    }

    protected void initialiseGame() {
        //TODO: Implement initializeGame()
    }
}
