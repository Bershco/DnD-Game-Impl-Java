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
    private MessageCallback messageCallback;

    public String defaultLevelPath;
    private File[] levels;
    private final List<DeathObserver> deathObservers = new LinkedList<>();
    private final List<WinObserver> winObservers = new LinkedList<>();
    private int currLevel = 0;

    public GameMaster(MessageCallback messageCallback){
        this.messageCallback = messageCallback;
    }

    public void initialiseGame(String path) {
        player = new Player("default_player",1,1,1,-1,-1);
        defaultLevelPath = path;
        levels = ((new File(defaultLevelPath)).listFiles());
        if (levels != null)
            Arrays.sort(levels); //TODO: check what happens with more than 10 level files (i.e more than a single digit level)
        else
            throw new IllegalArgumentException("Something went wrong while loading level files.");
        board = new Board(readNextLevel());
        initialiseLevel();
    }

    public void initialiseLevel() {
        for (Enemy e : enemies)
            e.addDeathObserver(this);
        initialiseTileSurroundings();
        initialiseEnemySurroundings();
    }
    public void initialiseEnemySurroundings() {
        for (Enemy curr : enemies) {
            curr.setSurroundings(board.getSurroundings(curr.pos));
        }
    }
    public void initialiseTileSurroundings() {
        for (Tile[] tArray : board.currentPosition)
            for (Tile t : tArray)
                t.setSurroundings(board.getSurroundings(t.pos));
    }
    public void initialisePlayer(int playerInt) {
        player = txtToPlayer(playerInt,player.pos.x,player.pos.y);
        player.setSurroundings(board.getSurroundings(player.pos));
        player.setMessageCallback(messageCallback);
        player.addDeathObserver(this);
    }

    public String getPlayerDescription() { return player.description();}
    public String getCurrentBoard() { return board.description();}
    public String getPlayerName() { return player.getName();}
    public String getDefaultPlayerPath() {
        return defaultPlayerPath;
    }

    public String onGameTick(Action a) { //TODO maybe check if String return type is needed or can be void
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
                goTo(player,a);
        }
        List<Unit> onlyThePlayer = new LinkedList<>();
        onlyThePlayer.add(player);
        for (Enemy e : enemies) {
            Action enemyMovement = e.onGameTick(onlyThePlayer);
            if (enemyMovement != Action.STAND)
                goTo(e,enemyMovement);
        }
        if (enemies.isEmpty())
            onLevelWon();
        return "";
    }

    public void goTo(Enemy curr, Action direction) {
        //This method is for readability purposes.
        Tile temp = curr;
        switch (direction) {
            case UP -> temp = curr.getAbove();
            case LEFT -> temp = curr.getOnTheLeft();
            case DOWN -> temp = curr.getBelow();
            case RIGHT -> temp = curr.getOnTheRight();
        }
        if (curr.visit(temp))
            board.swapTiles(curr,temp);
    }

    public void goTo(Player curr, Action direction) {
        //This method is for readability purposes.
        Tile temp = curr;
        switch (direction) {
            case UP -> temp = curr.getAbove();
            case LEFT -> temp = curr.getOnTheLeft();
            case DOWN -> temp = curr.getBelow();
            case RIGHT -> temp = curr.getOnTheRight();
        }
        if (curr.visit(temp))
            board.swapTiles(curr,temp);
    }



    public void gameOverLose() {
        Grave grave = board.replacePlayerWithGrave(player.pos);
        grave.setSurroundings(player.getSurroundings());
        grave.updateTheSurroundings();
        //TODO: UI should output final board layout using board.description() - make sure both the "WIN/LOSE" board and the final board are output.
    }

    /**
     * Loads the win board
     */
    public void loadWin() {
        board = new Board(readNextLevel());
    }

    /**
     * Loads the lose board
     */
    public void loadLose() {
        currLevel = -1;
        board = new Board(readNextLevel());
    }

    public void onLevelWon() {
        board.currentPosition = readNextLevel();
        initialiseLevel();
    }
    @Override
    public void onPlayerEvent(Unit killer) {
        gameOverLose();
        notifyDeathObservers(killer,player.getPos());
    }
    @Override
    public void onEnemyEvent(Enemy e) {
        Empty empty = board.replaceEnemyWithEmpty(e.pos); //TODO update the surroundings
        empty.setSurroundings(e.getSurroundings());
        empty.updateTheSurroundings();
        player.addExp(e.getExperienceValue());
        enemies.remove(e);
    }
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
    private Tile[][] readNextLevel() {
        if (currLevel >= levels.length) {
            notifyWinObservers(true);
            levels = new File[1];
            levels[0] = new File(defaultWinLevelPath);
            currLevel = 0;
        }
        else if (currLevel < 0) {
            //notifyDeathObservers(player, );
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
            System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        enemies.clear();
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