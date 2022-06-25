import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board b;
    Enemy e1 = new Enemy("dai",'d',100,69,69,420);
    Enemy basicallyMeme = new Enemy("Lowki",'L',Integer.MAX_VALUE,42,0,69420);
    ArrayList<Enemy> allEnemies;
    ArrayList<Enemy> closeEnemies;
    @BeforeEach
    void setUp() {
        b = Board.getInstance();
        allEnemies.add(e1);
        closeEnemies = b.getEnemies(Integer.MAX_VALUE);
        allEnemies.add(basicallyMeme);
    }

    @Test
    void getEnemies() {
        //TODO: Implement getEnemies(int range) test
    }

    @Test
    void removeEnemy() {
        assertEquals(closeEnemies,b.getEnemies(Integer.MAX_VALUE));
        b.removeEnemy(e1);
        assertNotEquals(closeEnemies,b.getEnemies(Integer.MAX_VALUE));
        allEnemies.add(e1);
        b.removeEnemy(basicallyMeme);
        assertNotEquals(closeEnemies,b.getEnemies(Integer.MAX_VALUE));
        //TODO: get Tom's approval of this test, I'm not sure it's enough but it seems alright
    }

    @Test
    void onGameTick() {
    }

    @Test
    void gameOverLose() {
        boolean graveCheck = false;
        for (int i = 0; !graveCheck && i < b.currentPosition.length; i++)
            for (int j = 0; !graveCheck && j < b.currentPosition[i].length; j++)
                if (b.currentPosition[i][j].tile == 'X') graveCheck = true;
        assertTrue(graveCheck);
    }

    @Test
    void gameOverWin() {
        //TODO: Implement gameOverWin() test
    }

    @Test
    void initialiseGame() {
        //TODO: Implement initialiseGame() test
    }
}