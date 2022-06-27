package UI;
import Backend.*;

import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;

public class PlayerUI {
    private final GameMaster gm = new GameMaster();

    public void initialisePlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("CHOOSE YOUR FIGHTER:");
        printFighters();
        gm.initialisePlayer(Integer.parseInt(scanner.nextLine()));
        System.out.println("YOUR CHOSEN FIGHTER IS :");
        System.out.println(gm.getPlayerDescription()); //TODO: Decide later about whether or not we should change to just the name
    }

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
            String line = lines.removeFirst(); //TODO: check it's not removeLast()
            String[] playerDescription = line.split(","); //TODO: check if regex works
            String playerClass = playerDescription[1];
            System.out.println();
            System.out.println("======= Press " + counter++ + " for ===");
            System.out.println("Name : " + playerDescription[2]);
            System.out.println("Class : " + playerClass);
            System.out.println("Max. Health : " + playerDescription[3]);
            System.out.println("Attack Damage : " + playerDescription[4]);
            System.out.println("Defense Points : " + playerDescription[5]);
            switch (playerClass) {
                case "Rogue" -> System.out.println("Ability Cost : " + playerDescription[6]);
                case "Warrior" -> System.out.println("Ability Cooldown : " + playerDescription[6]);
                case "Hunter" -> System.out.println("Ability Range : " + playerDescription[6]);
                case "Mage" -> {
                    System.out.println("Starting Mana : " + (Integer.parseInt(playerDescription[6]))/4);
                    System.out.println("Max. Mana : " + playerDescription[6]);
                    System.out.println("Ability Cost : " + playerDescription[7]);
                    System.out.println("Spell Power : " + playerDescription[8]);
                    System.out.println("Spell Hits : " + playerDescription[9] + " times");
                    System.out.println("Ability Range : " +playerDescription[10]);
                }
            }
            System.out.println("===============================");
            System.out.println();
            System.out.println();
        }
    }

    public void printCurrPlayerDesc() {
        System.out.println(gm.getPlayerDescription());
    }

    public String getPlayerName() {
        return gm.getPlayerName();
    }

}
