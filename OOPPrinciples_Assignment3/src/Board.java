import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Board instance;
    public Tile[][] currentPosition;
    private ArrayList<Enemy> allEnemies;
    private ArrayList<Wall> allWalls;
    private Player player;

    /**
     * The board is a singleton.
     */
    private Board(){

    }
    

    /**
     * This method returns an instance of the player being played by the user
     * @return an instance of the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * This method returns an instance of the board as it is a singleton and shouldn't occur more than once
     * @return an instance of the current board
     */
    public static Board getInstance() {
        if(instance == null)
            instance = new Board();
        return instance;
    }


    /**
     * This method is a helper method to castAbility() in types of Players that need a list of enemies in a valid range
     * @param range the valid range
     * @return a list of enemies in given range
     */
    public ArrayList<Enemy> getEnemies(int range) {
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
     * @param e
     */
    public void removeEnemy(Enemy e) {
        allEnemies.remove(e);
    }

    /**
     * This method acts after a player action - calling all enemies to play their turn
     */
    public void onGameTick() {
        for (Enemy e : allEnemies) {
            e.onGameTick();
        }
    }
    public void gameOverLose() {
        Tile grave = new Grave(player.pos);
        currentPosition[grave.pos.x][grave.pos.y] = grave;
        //TODO: provide error message
    }
}
