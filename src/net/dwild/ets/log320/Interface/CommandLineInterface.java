package net.dwild.ets.log320.Interface;

public class CommandLineInterface {

    private static final int NOIR = 2;
    private static final int BLANC = 4;

    public void drawBoard(int[][] board) {
        String toPrint = "";
        for(int y=7; y>=0;y--) {
            toPrint += y+1 + "\t";
            for (int x=0; x<8;x++) {
                if (board[x][y] == NOIR) {
                    toPrint += "O\t";
                }
                else if (board[x][y] == BLANC) {
                    toPrint += "X\t";
                }
                else {
                    toPrint += ".\t";
                }
            }
            toPrint += "\n";
        }
        toPrint += "\n\tA\tB\tC\tD\tE\tF\tG\tH\n";
        System.out.println(toPrint);
    }
}
