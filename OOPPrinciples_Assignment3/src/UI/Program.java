package UI;
import Backend.GameMaster;

import java.util.Scanner;
public class Program {

    public static void main(String[] args) {
        GameUI gui = new GameUI(args[0]);
        printTutorial();
        gui.startGame();
        //TODO: UML - Tom
        //TODO: Unit tests - if there's time
    }

    public static void printTutorial() {
        System.out.println(
                "=============================== \n" +
                "The game is pretty straight-forward, kill enemies in order to progress levels. \n" +
                "Kill enemies by walking at them (melee attack) or using your ability. \n" +
                "The controls are : \n" +
                "W -> move upwards \n" +
                "A -> move leftwards \n" +
                "S -> move downwards \n" +
                "D -> move rightwards \n" +
                "E -> cast special ability \n" +
                "Q -> stay in place \n" +
                "Remember to press enter after each input. \n\n" +
                "Try not to die. \n" +
                "===============================");
    }
}
