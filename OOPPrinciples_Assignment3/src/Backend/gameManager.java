package Backend;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class gameManager implements DeathObserver,Observable{
    private Player player;
    private Board board;
    private final LinkedList<Enemy> enemies = new LinkedList<>();
    private final static String defaultMobPath = (new File("mobs.txt")).getPath(); //TODO: add all mobs from list
    private final static String defaultBossPath = (new File("bosses.txt")).getPath(); //TODO: add all bosses from list
    private final static String defaultPlayerPath = (new File("players.txt")).getPath(); //TODO: add all players from list
    public String defaultLevelPath;
    private final List<DeathObserver> deathObservers = new LinkedList<>();
    private final List<WinObserver> winObservers = new LinkedList<>();
    private boolean loaded = false;

    public void initialiseGame(String path) { //TODO: receive path from command line arguments
        player = new Player("default_player",1,1,1,-1,-1);
        board = new Board(readNextLevel());
        player.addDeathObserver(this);
        defaultLevelPath = path;
    }
    public void initialisePlayer(int playerInt) {
        player = txtToPlayer(playerInt,player.pos.x,player.pos.y);
    }
    /*
    public void attemptSwapTiles() {
        if (player.swap(player.getAbove())) {
            //board.swap();
        }
    }
     */

    public String getPlayerDescription() { return player.description();}
    public String getCurrentBoard() { return board.description();}
    public String getPlayerName() { return player.getName();}
    public String getDefaultPlayerPath() {
        return defaultPlayerPath;
    }
    public boolean getLoaded() {
        return loaded;
    }

    public void onGameTick(Action a) {
        player.onGameTick(a);
        List<Unit> onlyThePlayer = new LinkedList<>();
        onlyThePlayer.add(player);
        for (Enemy e : enemies) {
            e.onGameTick(onlyThePlayer);
        }
        if (enemies.isEmpty())
            onLevelWon();
    }

    public void gameOverLose() {
        board.replacePlayerWithGrave(player.pos);
        notifyDeathObservers();
        //TODO: UI should output "you have lost" or smth like that
        //TODO: UI should output final board layout using board.description()
    }
    public void onLevelWon() {
        readNextLevel();
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
            while ((line = reader.readLine()) != null) { //if we want to use more than 10 players, just swap this while with a for loop
                if (line.charAt(0) == playerInt) {
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
    private Monster txtToMonster(char mobType, int x, int y) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultMobPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == mobType) {
                    String[] mobDescription = line.split(","); //TODO: check if regex works
                    char tile = mobDescription[0].charAt(0);
                    String name = mobDescription[1];
                    int health = Integer.parseInt(mobDescription[2]);
                    int attack = Integer.parseInt(mobDescription[3]);
                    int defense = Integer.parseInt(mobDescription[4]);
                    int visionRange = Integer.parseInt(mobDescription[5]);
                    int exp = Integer.parseInt(mobDescription[6]);
                    return new Monster(name,tile,health,attack,defense,exp,visionRange,x,y);
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalArgumentException("Something went wrong while importing mobs from text");
    }
    private Boss txtToBoss(char bossType, int x, int y) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultBossPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == bossType) {
                    String[] bossDescription = line.split(","); //TODO: check if regex works
                    char tile = bossDescription[0].charAt(0);
                    String name = bossDescription[1];
                    int health = Integer.parseInt(bossDescription[2]);
                    int attack = Integer.parseInt(bossDescription[3]);
                    int defense = Integer.parseInt(bossDescription[4]);
                    int visionRange = Integer.parseInt(bossDescription[5]);
                    int exp = Integer.parseInt(bossDescription[6]);
                    int ability = Integer.parseInt(bossDescription[7]);
                    return new Boss(name,tile,health,attack,defense,visionRange,exp,ability,x,y);
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalArgumentException("Something went wrong while importing mobs from text");
    }
    private Tile processTile(char c, int x, int y) {
        if (c == '.') return new Empty(new Position(x,y));
        else if (c == '#') return new Wall(new Position(x,y));
        else if (c == '@') {
            player.setPos(x,y);
            return player;
        }
        else if (c >= 'a' && c<= 'z') {
            Monster curr = txtToMonster(c,x,y);
            enemies.add(curr);
            return curr;
        }
        else if (c >= 'A' && c<= 'Z') {
            Boss curr = txtToBoss(c,x,y);
            enemies.add(curr);
            return curr;
        }
        else {
            throw new IllegalArgumentException("Something went wrong while processing tiles");
        }
    }
    private Tile[][] readNextLevel() {
        LinkedList<String> lines = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultLevelPath+ ++board.currLevel+".txt")); //TODO: make sure the given path trough cmd line args works
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            loaded = true; //TODO: think of a more elegant way to notify win observers
        } catch (FileNotFoundException fileNotFoundException) {
            if (!loaded)
                System.out.println("File not found as expected. (level"+board.currLevel+".txt)");
            else
                notifyWinObservers();
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        Tile[][] boardTiles = new Tile[lines.size()][];
        int lineCount = 0;
        while (!lines.isEmpty()) {
            String currLine = lines.removeFirst(); //TODO: make sure this is the proper one, could be removeLast
            char[] currLineInChar = currLine.toCharArray();
            Tile[] currLineInTiles = new Tile[currLine.length()];
            for (int column = 0; column < currLineInChar.length; column++) {
                currLineInTiles[column] = processTile(currLineInChar[column], column, lineCount);
            }
            boardTiles[lineCount++] = currLineInTiles;
        }
        loaded = false;
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
    public void notifyWinObservers() {
        for (WinObserver winObserver : winObservers)
            winObserver.onWinEvent();
    }
}