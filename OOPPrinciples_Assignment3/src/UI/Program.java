package UI;

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
                """
                        ===============================
                        The game is pretty straight-forward, kill enemies in order to progress levels.\s
                        Kill enemies by walking at them (melee attack) or using your ability.\s
                        The controls are :\s
                        W -> move upwards\s
                        A -> move leftwards\s
                        S -> move downwards\s
                        D -> move rightwards\s
                        E -> cast special ability\s
                        Q -> stay in place\s
                        Remember to press enter after each input.\s

                        Try not to die.\s
                        ===============================""");
    }
}
