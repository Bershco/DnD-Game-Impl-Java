package UI;
import Backend.*;

import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;

public class PlayerUI {
    private final GameMaster gm;

    //Constructor
    public PlayerUI(GameMaster gm){
        this.gm = gm;
    }

    /**
     * This method initialises the player
     */
    protected void initialisePlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("CHOOSE YOUR FIGHTER:");
        printFighters();
        gm.initialisePlayer(Integer.parseInt(scanner.nextLine()));
        System.out.println("YOUR CHOSEN FIGHTER IS :");
        gm.printPlayerDescription();
    }

    /**
     * This method prints the available fighters from text
     */
    private void printFighters() {
        String fighterPath = gm.getDefaultPlayerPath();
        LinkedList<String> lines = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fighterPath));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }
        int counter = 1;
        while (!lines.isEmpty()) {
            String line = lines.removeFirst();
            String[] playerDescription = line.split(",");
            String playerClass = playerDescription[1];
            System.out.println();
            System.out.println("========= Press " + counter++ + " for =========");
            System.out.println("\tName : " + playerDescription[2]);
            System.out.println("\tClass : " + playerClass);
            System.out.println("\tMax. Health : " + playerDescription[3]);
            System.out.println("\tAttack Damage : " + playerDescription[4]);
            System.out.println("\tDefense Points : " + playerDescription[5]);
            switch (playerClass) {
                case "Rogue" -> System.out.println("\tAbility Cost : " + playerDescription[6]);
                case "Warrior" -> System.out.println("\tAbility Cooldown : " + playerDescription[6]);
                case "Hunter" -> System.out.println("\tAbility Range : " + playerDescription[6]);
                case "Mage" -> {
                    System.out.println("\tStarting Mana : " + (Integer.parseInt(playerDescription[6]))/4);
                    System.out.println("\tMax. Mana : " + playerDescription[6]);
                    System.out.println("\tAbility Cost : " + playerDescription[7]);
                    System.out.println("\tSpell Power : " + playerDescription[8]);
                    System.out.println("\tSpell Hits : " + playerDescription[9] + " times");
                    System.out.println("\tAbility Range : " +playerDescription[10]);
                }
            }
            System.out.println("===============================");
            System.out.println();
            System.out.println();
        }
    }

    /**
     * This method prints the chosen player description
     */
    protected void printCurrPlayerDesc() {
        gm.printPlayerDescription();
    }

    /**
     * This method retrieves the player's name
     * @return the player's name
     */
    protected String getPlayerName() {
        return gm.getPlayerName();
    }
}
