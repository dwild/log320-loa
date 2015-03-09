package net.dwild.ets.log320.Interface;

import net.dwild.ets.log320.GameLogic.Board;

public class CommandLineInterface {

    public void drawBoard(Board board) {
        for (int y = 0; y < 8; y++) {
            System.out.print((8 - y) + "\t");

            for (int x = 0; x < 8; x++) {
                if (board.get(x, y) == Board.BLACK) {
                    System.out.print("O\t");
                } else if (board.get(x, y) == Board.WHITE) {
                    System.out.print("X\t");
                } else {
                    System.out.print(".\t");
                }
            }

            System.out.println();
        }

        System.out.println("\n\tA\tB\tC\tD\tE\tF\tG\tH\n");
    }
}
