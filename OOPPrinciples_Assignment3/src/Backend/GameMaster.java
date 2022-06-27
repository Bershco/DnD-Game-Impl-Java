package Backend;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameMaster implements DeathObserver,Observable{
    private Player player;
    private Board board;
    private final LinkedList<Enemy> enemies = new LinkedList<>();
    private final static String defaultEnemyPath = (new File("enemies.txt")).getPath(); //TODO: add all mobs from list
    private final static String defaultPlayerPath = (new File("players.txt")).getPath(); //TODO: add all players from list
    private final static String defaultWinLevelPath = (new File("dontREADME.txt")).getPath(); //TODO: modify the text file
    public String defaultLevelPath;
    private File[] levels;
    private final List<DeathObserver> deathObservers = new LinkedList<>();
    private final List<WinObserver> winObservers = new LinkedList<>();
    private int currLevel = 0;

    public void initialiseGame(String path) {
        player = new Player("default_player",1,1,1,-1,-1);
        defaultLevelPath = path;
        levels = ((new File(defaultLevelPath)).listFiles());
        if (levels != null)
            Arrays.sort(levels); //TODO: check what happens with more than 10 level files (i.e more than a single digit level)
        else
            throw new IllegalArgumentException("Something went wrong while loading level files.");
        board = new Board(readNextLevel());
    }
    public void initialisePlayer(int playerInt) {
        player = txtToPlayer(playerInt,player.pos.x,player.pos.y);
        player.addDeathObserver(this);
    }
    /*
    public void attemptSwapTiles() {
        if (player.swap(player.getAbove())) {
            //board.swap();
        }
    } TODO: might not be needed
     */

    public String getPlayerDescription() { return player.description();}
    public String getCurrentBoard() { return board.description();}
    public String getPlayerName() { return player.getName();}
    public String getDefaultPlayerPath() {
        return defaultPlayerPath;
    }


    public String onGameTick(Action a) {
        if (a == Action.ABILITYCAST) {
            try {
                player.castAbility(enemies);
            }
            catch (Exception e) {
                return e.getMessage();
            }
        }
        else {
            if (player.onGameTick(a) != Action.STAND)
                board.swapTiles(player.pos, a);
        }
        List<Unit> onlyThePlayer = new LinkedList<>();
        onlyThePlayer.add(player);
        for (Enemy e : enemies) {
            Action enemyMovement = e.onGameTick(onlyThePlayer);
            if (enemyMovement != Action.STAND)
                board.swapTiles(e.pos,enemyMovement);
        }
        if (enemies.isEmpty())
            onLevelWon();
        return "";
    }

    public void gameOverLose() {
        board.replacePlayerWithGrave(player.pos);
        notifyDeathObservers();
        //TODO: UI should output "you have lost" or smth like that
        //TODO: UI should output final board layout using board.description()
    }
    public void onLevelWon() {
        board.currentPosition = readNextLevel();
    }
    @Override
    public void onPlayerEvent() {
        gameOverLose();
    }
    @Override
    public void onEnemyEvent(Enemy e) {
        board.replaceEnemyWithEmpty(e.pos);
        enemies.remove(e);
    }
    private Player txtToPlayer(int playerInt, int x, int y) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultPlayerPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == playerInt) { //if we want to use more than 10 players, just swap this while with a for loop
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
    } //TODO: add all player options to players.txt
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
    } //TODO: add all traps and bosses to enemies.txt
    private Tile processTile(char c, int x, int y) {
        if (c == '.') return new Empty(new Position(x,y));
        else if (c == '#') return new Wall(new Position(x,y));
        else if (c == '@') {
            player.setPos(x,y);
            return player;
        }
        else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            Enemy curr = txtToEnemy(c,x,y);
            enemies.add(curr);
            return curr;
        }
        else {
            throw new IllegalArgumentException("Something went wrong while processing tiles");
        }
    }
    private Tile[][] readNextLevel() {
        if (currLevel >= levels.length) {
            notifyWinObservers(true);
            levels = new File[1];
            levels[0] = new File(defaultWinLevelPath);
            currLevel = 0;
        }
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
            System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        Tile[][] boardTiles = new Tile[lines.size()][]; //TODO: switch all xs with ys
        int y = 0;
        while (!lines.isEmpty()) {
            String currLine = lines.removeFirst(); //TODO: make sure this is the proper one, could be removeLast
            char[] currLineInChar = currLine.toCharArray();
            Tile[] currLineInTiles = new Tile[currLine.length()];
            for (int x = 0; x < currLineInChar.length; x++) {
                currLineInTiles[x] = processTile(currLineInChar[x], x, y);
            }
            boardTiles[y++] = currLineInTiles;
        }
        return boardTiles;
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
    public void notifyDeathObservers() {
        for (DeathObserver deathObserver : deathObservers)
            deathObserver.onPlayerEvent();
    }
    @Override
    public void notifyWinObservers(boolean endGame) {
        for (WinObserver winObserver : winObservers)
            winObserver.onWinEvent(endGame);
    }
}