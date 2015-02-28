package net.dwild.ets.log320.loa;


public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        CommandLineInterface commandLineInterface = new CommandLineInterface();
        commandLineInterface.drawBoard(board);
    }

}
