package Backend;

import Backend.Tiles.Empty;
import Backend.Tiles.Grave;
import Backend.Tiles.Tile;

public class Board {
    protected Tile[][] currentPosition;

    //Constructor
    public Board(Tile[][] t){
        currentPosition = t;
    }

    //Getters
    public Tile[] getSurroundings(Position pos) {
        Tile[] output = new Tile[4];
        output[0] = (pos.getY() == 0) ? null : currentPosition[pos.getY()-1][pos.getX()];
        output[1] = (pos.getX() == 0) ? null : currentPosition[pos.getY()][pos.getX()-1];
        output[2] = (pos.getY() == currentPosition.length-1) ? null : currentPosition[pos.getY()+1][pos.getX()];
        output[3] = (pos.getX() == currentPosition[pos.getY()].length-1) ? null : currentPosition[pos.getY()][pos.getX()+1];
        return output;
    }
    public Tile[][] getCurrentPosition() {
        return currentPosition;
    }

    /**
     * This method replaces a dead enemy with an Empty Tile in its position
     * @param enemyPos the position of the dead enemy
     * @return the Empty Tile
     */
    public Tile replaceEnemyWithEmpty(Position enemyPos) {
        Empty newTile = new Empty(enemyPos);
        currentPosition[enemyPos.getY()][enemyPos.getX()] = newTile;
        return newTile;
    }
    /**
     * This method replaces a dead player with a Grave Tile in its position
     * @param playerPos the position of the dead player
     * @return the Grave Tile
     */
    public Grave replacePlayerWithGrave(Position playerPos) {
        Grave grave = new Grave(playerPos);
        currentPosition[playerPos.getY()][playerPos.getX()] = grave;
        return grave;
    }
    /**
     * This method describes the entirety of the board, and outputs it into String form
     * @return a String form board using the Tiles' chars
     */
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
    /**
     * This method replaces Tiles between positions in the 2D array
     * @param curr Tile 1 to be replaced
     * @param desired Tile 2 to be replaced
     */
    public void swapTiles(Tile curr, Tile desired) {
        currentPosition[curr.getY()][curr.getX()] = curr;
        currentPosition[desired.getY()][desired.getX()] = desired;
    }
}