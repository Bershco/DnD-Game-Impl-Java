import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Board instance;
    public Tile[][] currentPosition;
    private ArrayList<Enemy> allEnemies;
    private ArrayList<Wall> allWalls;
    private Player player;

    private Board(){

    }

    public static Board getInstance() {
        if(instance == null)
            instance = new Board();
        return instance;
    }

    public ArrayList<Enemy> getEnemies(int range) {
        ArrayList<Enemy> closeEnemies = new ArrayList<>();
        allEnemies.forEach(enemy -> {
            if(player.range(enemy) < range)
                closeEnemies.add(enemy);
        });
        return closeEnemies;
    }
    public void removeEnemy(Enemy e) {
        allEnemies.remove(e);
    }
}
