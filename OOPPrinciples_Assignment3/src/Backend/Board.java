package Backend;

import java.io.*;
import java.util.*;


public class Board {
    protected Tile[][] currentPosition;
    public int currLevel;

    /**
     * The board is a singleton.
     */
    public Board(Tile[][] t){
        currLevel = 0;
        loadLevel(t);
    }

    public Tile[][] getCurrentPosition() {
        return currentPosition;
    }


    public void replaceEnemyWithEmpty(Position enemyPos) {
        currentPosition[enemyPos.x][enemyPos.y] = new Empty(enemyPos);
    }
    public void replacePlayerWithGrave(Position playerPos) {
        currentPosition[playerPos.x][playerPos.y] = new Grave(playerPos);
    }

    protected void loadLevel(Tile[][] t) {
        currentPosition = t;
    }

    public String description() {
        StringBuilder outputBoard = new StringBuilder();
        for (Tile[] t : currentPosition) {
            for (Tile tt : t) {
                outputBoard.append(tt.toString());
            }
            outputBoard.append("\n");
        }
        return outputBoard.toString();
    }
}