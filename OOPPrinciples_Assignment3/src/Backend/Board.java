package Backend;

import java.io.*;
import java.util.*;


public class Board {
    protected Tile[][] currentPosition;
    /**
     * The board is a singleton.
     */
    public Board(Tile[][] t){
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
        for (Tile[] tArray : currentPosition) {
            for (Tile t : tArray) {
                outputBoard.append(t.toString());
            }
            outputBoard.append("\n");
        }
        return outputBoard.toString();
    }
    public void swapTiles(Position pos, Action a) {
        Tile temp = currentPosition[pos.x][pos.y];
        switch (a) {
            case RIGHT -> {
                currentPosition[pos.x][pos.y] = currentPosition[pos.x+1][pos.y];
                currentPosition[pos.x+1][pos.y] = temp;
            }
            case LEFT -> {
                currentPosition[pos.x][pos.y] = currentPosition[pos.x-1][pos.y];
                currentPosition[pos.x-1][pos.y] = temp;
            }
            case DOWN -> {
                currentPosition[pos.x][pos.y] = currentPosition[pos.x][pos.y+1];
                currentPosition[pos.x][pos.y+1] = temp;
            }
            case UP -> {
                currentPosition[pos.x][pos.y] = currentPosition[pos.x][pos.y-1];
                currentPosition[pos.x][pos.y-1] = temp;
            }
        }
    }
}