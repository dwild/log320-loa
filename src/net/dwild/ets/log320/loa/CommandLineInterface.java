package net.dwild.ets.log320.loa;

public class CommandLineInterface {

    int NOIR = 2;
    int BLANC = 4;

    public CommandLineInterface() {
    }

    public void drawBoard(Board board) {
        int[][] configuration = board.boardConfiguration;
        String toPrint = "";
        for(int y=7; y>=0;y--) {
            toPrint += y+1 + "\t";
            for (int x=0; x<8;x++) {
                if (configuration[x][y] == NOIR) {
                    toPrint += "O\t";
                }
                else if (configuration[x][y] == BLANC) {
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
