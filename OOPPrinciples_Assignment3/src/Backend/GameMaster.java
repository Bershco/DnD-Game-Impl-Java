package Backend;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameMaster implements DeathObserver,Observable{
    private Player player;
    private Board board;
    private final LinkedList<Enemy> enemies = new LinkedList<>();
    private final static String defaultEnemyPath = (new File("enemies.txt")).getPath();
    private final static String defaultPlayerPath = (new File("players.txt")).getPath();
    private final static String defaultWinLevelPath = (new File("dontREADME.txt")).getPath();
    private final static String defaultLoseLevelPath = (new File("dontREADME2.txt")).getPath();
    private final MessageCallback messageCallback;
    private String defaultLevelPath;
    private File[] levels;
    private final List<DeathObserver> deathObservers = new LinkedList<>();
    private final List<WinObserver> winObservers = new LinkedList<>();
    private int currLevel = 0;

    //Constructor
    public GameMaster(MessageCallback messageCallback){
        this.messageCallback = messageCallback;
    }

    //Getters
    public String getPlayerDescription() { return player.description();}
    public String getCurrentBoard() { return board.description();}
    public String getPlayerName() { return player.getName();}
    public String getDefaultPlayerPath() {
        return defaultPlayerPath;
    }

    /**
     * This method initialises the game using the proper level directory path
     * @param path the proper path to the level directory
     */
    public void initialiseGame(String path) {
        player = new Player("default_player",1,1,1,-1,-1);
        defaultLevelPath = path;
        levels = ((new File(defaultLevelPath)).listFiles());
        if (levels != null)
            Arrays.sort(levels); //TODO: check what happens with more than 10 level files (i.e more than a single digit level) //MAYBE NOT NEEDED
        else
            throw new IllegalArgumentException("Something went wrong while loading level files.");
        board = new Board(readNextLevel());
        initialiseLevel();
    }

    /**
     * This method loads the next level from the text files in defaultLevelDirectory
     * @return a 2D Tile array to be used as a Board
     */
    private Tile[][] readNextLevel() {
        if (currLevel >= levels.length) {
            notifyWinObservers(true);
            levels = new File[1];
            levels[0] = new File(defaultWinLevelPath);
            currLevel = 0;
        } else if (currLevel < 0) {
            //notifyDeathObservers(player, ); TODO: check if this commented line is needed, I think not.
            levels = new File[1];
            levels[0] = new File(defaultLoseLevelPath);
            currLevel = 0;
        } else notifyWinObservers(false);

        LinkedList<String> lines = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(levels[currLevel++]));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found as expected. (level"+currLevel+".txt)");
        } catch (IOException e) {
            messageCallback.send(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        enemies.clear();
        Tile[][] boardTiles = new Tile[lines.size()][];
        int row = 0;
        while (!lines.isEmpty()) {
            String currLine = lines.removeFirst(); //TODO: make sure this is the proper one, could be removeLast
            char[] currLineInChar = currLine.toCharArray();
            Tile[] currLineInTiles = new Tile[currLine.length()];
            for (int column = 0; column < currLineInChar.length; column++) {
                currLineInTiles[column] = processTile(currLineInChar[column], column, row);
            }
            boardTiles[row++] = currLineInTiles;
        }
        return boardTiles;
    }

    /**
     * This is a helper method that helps initialise all
     */
    private void initialiseLevel() {
        for (Enemy e : enemies)
            e.addDeathObserver(this);
        initialiseTileSurroundings();
        //initialiseEnemySurroundings(); TODO: check if needed, I think not.
    }
    /**
     * This method initialises all surroundings of every Tile in the 2D Tile array
     */
    private void initialiseTileSurroundings() {
        for (Tile[] tArray : board.currentPosition)
            for (Tile t : tArray)
                t.setSurroundings(board.getSurroundings(t.getPos()));
    }
//    private void initialiseEnemySurroundings() {
//        for (Enemy curr : enemies) {
//            curr.setSurroundings(board.getSurroundings(curr.getPos()));
//        }
//    }

    /**
     * This method initialises the player properly using another method
     * @param playerInt the index of the player chosen by the actual player
     */
    public void initialisePlayer(int playerInt) {
        player = txtToPlayer(playerInt,player.getX(), player.getY());
        player.setSurroundings(board.getSurroundings(player.getPos()));
        player.setMessageCallback(messageCallback);
        player.addDeathObserver(this);
    }

    /**
     * This method enacts what is supposed to happen after every choice for a turn the player makes
     * @param a the action to be performed
     */
    public void onGameTick(Action a) {
        if (a == Action.ABILITYCAST) {
            try {
                player.castAbility(enemies);
            }
            catch (Exception e) {
                messageCallback.send(e.getMessage());
            }
        }
        else {
            if (a != Action.STAND)
                goTo(player,a);
        }
        List<Unit> onlyThePlayer = new LinkedList<>();
        onlyThePlayer.add(player);
        for (Enemy e : enemies) {
            Action enemyMovement = e.onGameTick(onlyThePlayer);
            if (enemyMovement != Action.STAND)
                goTo(e,enemyMovement);
        }
        player.onGameTick(a);
        if (enemies.isEmpty())
            onLevelWon();
    }

    /**
     * This method
     * @param curr the unit that is enacting to go somewhere
     * @param direction the direction the unit wants to go towards
     */
    private void goTo(Unit curr, Action direction) {
        //This method is for readability purposes.
        Tile temp = curr;
        switch (direction) {
            case UP -> temp = curr.getAbove();
            case LEFT -> temp = curr.getOnTheLeft();
            case DOWN -> temp = curr.getBelow();
            case RIGHT -> temp = curr.getOnTheRight();
        }
        if (curr.goTo(temp))
            board.swapTiles(curr,temp);
    }

    /**
     * This method describes what happens in a loss scenario
     */
    public void gameOverLose() {
        Grave grave = board.replacePlayerWithGrave(player.getPos());
        grave.setSurroundings(player.getSurroundings());
        grave.updateTheSurroundings();
    }

    /**
     * Loads the "YOU WIN..." board
     */
    public void loadWin() {
        board = new Board(readNextLevel());
    }

    /**
     * Loads the "YOU LOSE..." board
     */
    public void loadLose() {
        currLevel = -1;
        board = new Board(readNextLevel());
    }

    /**
     * Loads the next level and initialises it, happens when the player finished a level but not yet the game.
     */
    public void onLevelWon() {
        board.currentPosition = readNextLevel();
        initialiseLevel();
    }

    /**
     * This method constructs the proper player the user picked in its proper position on the board
     * @param playerInt the index of the player the user has picked
     * @param x the x (column) position of the player
     * @param y the y (row) position of the player
     * @return the Player created
     */
    private Player txtToPlayer(int playerInt, int x, int y) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultPlayerPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (Integer.parseInt(""+line.charAt(0)) == playerInt) { //if we want to use more than 10 players, just swap this while with a for loop
                    String[] playerDescription = line.split(","); //TODO: check if regex works
                    String playerClass = playerDescription[1];
                    String name = playerDescription[2];
                    int health = Integer.parseInt(playerDescription[3]);
                    int attack = Integer.parseInt(playerDescription[4]);
                    int defense = Integer.parseInt(playerDescription[5]);
                    switch (playerClass) {
                        case "Rogue" -> {
                            int cost = Integer.parseInt(playerDescription[6]);
                            return new Rogue(name,health,attack,defense,cost,x,y);
                        }
                        case "Warrior" -> {
                            int cd = Integer.parseInt(playerDescription[6]);
                            return new Warrior(name,health,attack,defense,cd,x,y);
                        }
                        case "Mage" -> {
                            int mp = Integer.parseInt(playerDescription[6]);
                            int mCost = Integer.parseInt(playerDescription[7]);
                            int spellPower = Integer.parseInt(playerDescription[8]);
                            int hitCount = Integer.parseInt(playerDescription[9]);
                            int abilityRange = Integer.parseInt(playerDescription[10]);
                            return new Mage(name,health,attack,defense,mp,mCost,spellPower,hitCount,abilityRange,x,y);
                        }
                        case "Hunter" -> {
                            int range = Integer.parseInt(playerDescription[6]);
                            return new Hunter(name,health,attack,defense,range,x,y);
                        }
                        default -> throw new IllegalArgumentException("There are only 4 types you could implement.");
                    }
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalArgumentException("Something went wrong while importing player from text");
    }

    /**
     * This method constructs the proper enemy the map is describing
     * @param c the enemy to be created
     * @param x the x (column) position of the enemy
     * @param y the y (row) position of the enemy
     * @return the Enemy created
     */
    private Enemy txtToEnemy(char c, int x, int y) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultEnemyPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == c) {
                    String[] enemyDescription = line.split(","); //TODO: check if regex works
                    String enemyType = enemyDescription[1];
                    String name = enemyDescription[2];
                    int health = Integer.parseInt(enemyDescription[3]);
                    int attack = Integer.parseInt(enemyDescription[4]);
                    int defense = Integer.parseInt(enemyDescription[5]);
                    int exp = Integer.parseInt(enemyDescription[6]);
                    switch (enemyType) {
                        case "Monster" -> {
                            int visionRange = Integer.parseInt(enemyDescription[7]);
                            return new Monster(name,c,health,attack,defense,exp,visionRange,x,y);
                        }
                        case "Boss" -> {
                            int visionRange = Integer.parseInt(enemyDescription[7]);
                            int abilityFreq = Integer.parseInt(enemyDescription[8]);
                            return new Boss(name,c,health,attack,defense,exp,visionRange,abilityFreq,x,y);
                        }
                        case "Trap" -> {
                            int visibilityTime = Integer.parseInt(enemyDescription[7]);
                            int invisibilityTime = Integer.parseInt(enemyDescription[8]);
                            return new Trap(name,c,health,attack,defense,exp,visibilityTime,invisibilityTime,x,y);
                        }
                        default -> throw new IllegalArgumentException("There are only 3 enemy types.");
                    }
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalArgumentException("Something went wrong while importing mobs from text");
    }

    /**
     * This method constructs each tile properly with regard to its char
     * @param c its char
     * @param x its x (column) position
     * @param y its y (row) position
     * @return the Tile created
     */
    private Tile processTile(char c, int x, int y) {
        if (c == '.') return new Empty(new Position(x,y));
        else if (c == '#') return new Wall(new Position(x,y));
        else if (c == '@') {
            player.setPos(x,y);
            return player;
        }
        else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            Enemy curr = txtToEnemy(c,x,y);
            curr.setMessageCallback(messageCallback);
            enemies.add(curr);
            return curr;
        }
        else {
            throw new IllegalArgumentException("Something went wrong while processing tiles");
        }
    }


    //Observer Pattern

    /**
     * This method describes what happens when a player dies with regard to the Game Master
     * @param killer the enemy that killed the player
     */
    @Override
    public void onPlayerEvent(Unit killer) {
        gameOverLose();
        notifyDeathObservers(killer,player.getPos());
    }
    /**
     * This method describes what happens when an enemy dies with regard to the Game Master
     * @param e the enemy that died
     */
    @Override
    public void onEnemyEvent(Enemy e) {
        Tile empty = board.replaceEnemyWithEmpty(e.getPos());
        empty.setSurroundings(e.getSurroundings());
        empty.updateTheSurroundings();
        enemies.remove(e);
    }

    @Override
    public void addDeathObserver(DeathObserver o) {
        deathObservers.add(o);
    }
    @Override
    public void addWinObserver(WinObserver o) {
        winObservers.add(o);
    }
    @Override
    public void notifyDeathObservers(Unit killer, Position DeathPos) {
        for (DeathObserver deathObserver : deathObservers)
            deathObserver.onPlayerEvent(killer);
    }
    @Override
    public void notifyWinObservers(boolean endGame) {
        for (WinObserver winObserver : winObservers)
            winObserver.onWinEvent(endGame);
    }
}