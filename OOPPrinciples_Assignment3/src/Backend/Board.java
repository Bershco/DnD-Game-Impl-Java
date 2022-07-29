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


    public Empty replaceEnemyWithEmpty(Position enemyPos) {
        Empty newTile = new Empty(enemyPos);
        currentPosition[enemyPos.y][enemyPos.x] = newTile;
        return newTile;
    }
    public Grave replacePlayerWithGrave(Position playerPos) {
        Grave grave = new Grave(playerPos);
        currentPosition[playerPos.y][playerPos.x] = grave;
        return grave;
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
    public void swapTiles(Tile curr, Tile desired) {
        currentPosition[curr.pos.y][curr.pos.x] = curr;
        currentPosition[desired.pos.y][desired.pos.x] = desired;
//        switch (a) {
//            case RIGHT -> {
//                currentPosition[pos.y][pos.x] = currentPosition[pos.y][pos.x+1];
//                currentPosition[pos.y][pos.x+1] = temp;
//            }
//            case LEFT -> {
//                currentPosition[pos.y][pos.x] = currentPosition[pos.y][pos.x-1];
//                currentPosition[pos.y][pos.x-1] = temp;
//            }
//            case DOWN -> {
//                currentPosition[pos.y][pos.x] = currentPosition[pos.y+1][pos.x];
//                currentPosition[pos.y+1][pos.x] = temp;
//            }
//            case UP -> {
//                currentPosition[pos.y][pos.x] = currentPosition[pos.y-1][pos.x];
//                currentPosition[pos.y-1][pos.x] = temp;
//            }
//        }
    }

    public Tile[] getSurroundings(Position pos) {
        Tile[] output = new Tile[4];
        output[0] = (pos.y == 0) ? null : currentPosition[pos.y-1][pos.x];
        output[1] = (pos.x == 0) ? null : currentPosition[pos.y][pos.x-1];
        output[2] = (pos.y == currentPosition.length-1) ? null : currentPosition[pos.y+1][pos.x];
        output[3] = (pos.x == currentPosition[pos.y].length-1) ? null : currentPosition[pos.y][pos.x+1];
        return output;
    }
}