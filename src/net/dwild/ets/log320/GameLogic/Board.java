package net.dwild.ets.log320.GameLogic;

import net.dwild.ets.log320.ClientData.Square;
import net.dwild.ets.log320.ClientData.TurnPlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Board implements Cloneable {

    public final static int NONE = 0;
    public final static int BLACK = 2;
    public final static int WHITE = 4;

    private int[][] board;

    private int[] lineCount;
    private int[] columnCount;
    private int[] diagonalRightCount;
    private int[] diagonalLeftCount;

    private final P[] potentialMoves = {
            new PV( 0,-1),
            new PV( 0, 1),
            new PH( 1, 0),
            new PH(-1, 0),
            new PDR(-1,-1),
            new PDR( 1, 1),
            new PDL(-1, 1),
            new PDL( 1,-1)
    };

    public Board() {
        this(new int[8][8]);
    }

    public Board(int[][] board) {
        this.board = board;

        countTokens();
    }

    private Board(int[][] board, int[] lineCount, int[] columnCount, int[] diagonalRightCount, int[] diagonalLeftCount) {
        this.board = board;
        this.lineCount = lineCount;
        this.columnCount = columnCount;
        this.diagonalRightCount = diagonalRightCount;
        this.diagonalLeftCount = diagonalLeftCount;
    }

    public int countTokens(int color){
        return getTokens(color).size();
    }

    private void countTokens() {
        lineCount = new int[8];
        columnCount = new int[8];
        diagonalRightCount = new int[15];
        diagonalLeftCount = new int[15];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) != NONE) {
                    lineCount[y]++;
                    columnCount[x]++;
                    diagonalRightCount[(x - y + 7)]++;
                    diagonalLeftCount[(14 - x - y)]++;
                }
            }
        }
    }

    private ArrayList<Square> getTokens(int color) {
        ArrayList<Square> positions = new ArrayList<Square>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) == color) {
                    positions.add(new Square(x, y));
                }
            }
        }
        return positions;
    }

    // Retourne le pourcentage des pions d'une équipe qui sont connectés entre eux.
    // Une connectivité de 1 signifie la victoire.
    public double checkConnectivity(int color){
        return this.checkConnectivity(color, false);
    }

    // Le flag verbose n'est utilisé que pour le debug
    public double checkConnectivity(int color, boolean verbose){
        ArrayList<Square> positions = getTokens(color);
        ArrayList<Square> checked = new ArrayList<Square>();
        ArrayList<Square> ignored = new ArrayList<Square>();
        double max = 0.00;
        int count = 0;
        for (Square pos1:positions){
            ArrayList<Square> line = new ArrayList<Square>();
            if (!checked.contains(pos1)){
                checked.add(pos1);
                if (verbose){
                    System.out.println("Adding " + pos1);
                }
                line.add(pos1);
                for (Square pos2:positions){
                    boolean add = false;
                    if (!checked.contains(pos2) && !line.contains(pos2)){
                        if (pos2.isAdjacent(pos1)){
                            if (verbose){
                                System.out.println(pos1 + " is adjacent to " + pos2);
                            }
                            checked.add(pos2);
                            add = true;
                        }
                        if (!add){
                            for (Square pos3:line){
                                if (!checked.contains(pos2)){
                                    if (pos2.isAdjacent(pos3)){
                                        if (verbose){
                                            System.out.println(pos2 + " is adjacent to already found " + pos3);
                                        }
                                        checked.add(pos2);
                                        add = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (add){
                            if (verbose){
                                System.out.println("Adding " + pos2);
                            }
                            line.add(pos2);
                        } else {
                            ignored.add(pos2);
                        }
                    }
                }
                for (Square other:ignored){
                    if (!line.contains(other)){
                        boolean add = false;
                        if (verbose){
                            System.out.println("Checking ignored square " + other);
                        }
                        for (Square found:line){
                            if (found.isAdjacent(other)){
                                if (verbose){
                                    System.out.println(other + " is adjacent to already found " + found);
                                }
                                add = true;
                                break;
                            }
                        }
                        if (add){
                            line.add(other);
                            checked.add(other);
                            if (verbose){
                                System.out.println("Adding " + other);
                            }
                        }
                    }
                }
                if (line.size() > 0){
                    count += 1;
                    //System.out.println(Integer.toString(line.size()) + " / " + Integer.toString(positions.size()) + " = " + Double.toString((double)line.size()/(double)positions.size()));
                    double value = (double)line.size()/(double)positions.size();
                    if (value > max){
                        max = value;
                    }
                    if (verbose){
                        System.out.println("Groupe " + Integer.toString(count));
                        for (Square sq:line){
                            System.out.print(sq + ", ");
                        }
                        System.out.println("");
                    }
                }
            }
        }

        return max/(double)count;
    }

    public double averageDistance(int token){
        ArrayList<Square> positions = getTokens(token);

        double distanceTotale = 0.00;
        ArrayList<Square> calculated = new ArrayList<Square>();

        for(Square pos:positions){
            calculated.add(pos);
            for(Square pos1:positions){
                if (!calculated.contains(pos1)){
                    distanceTotale += pos.distanceTo(pos1);
                }
            }
        }

        return distanceTotale/(double)positions.size();
    }

    public void move(TurnPlay turn) {
        Square startingPosition = turn.getFrom();
        Square endingPosition = turn.getTo();

        int sx = startingPosition.getX();
        int sy = startingPosition.getY();

        int tokenFrom = get(sx, sy);

        int ex = endingPosition.getX();
        int ey = endingPosition.getY();

        int tokenTo = get(ex, ey);
        if (tokenFrom != NONE && tokenFrom != tokenTo) {
            if (tokenTo == NONE) {
                lineCount[ey]++;
                columnCount[ex]++;
                diagonalRightCount[(ex - ey + 7)]++;
                diagonalLeftCount[(14 - ex - ey)]++;
            }

            set(startingPosition.getX(), startingPosition.getY(), NONE);
            set(endingPosition.getX(), endingPosition.getY(), tokenFrom);

            lineCount[sy]--;
            columnCount[sx]--;
            diagonalRightCount[(sx - sy + 7)]--;
            diagonalLeftCount[(14 - sx - sy)]--;
        }
    }



    public ArrayList<TurnPlay> allPossibleMoves(int color) {
        ArrayList<TurnPlay> possibleMoves = new ArrayList<TurnPlay>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) == color) {
                    Square from = new Square(x, y);
                    for(Square to:validMoves(from)) {
                        possibleMoves.add(new TurnPlay(from, to));
                    }
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<Square> validMoves(Square from) {
        ArrayList<Square> validMoves = new ArrayList<Square>();

        int x = from.getX();
        int y = from.getY();

        int tokenFrom = get(x, y);

        for(P potentialMove:potentialMoves) {
            int tokenCount = potentialMove.tokenCount(x, y);

            Square to = new Square( x + tokenCount * potentialMove.dx, y + tokenCount * potentialMove.dy);
            if (to.getX() >= 0 && to.getX() < 8 && to.getY() >= 0 && to.getY() < 8) {
                boolean possible = true;
                int nextX = x;
                int nextY = y;
                for (int i = 1; i < tokenCount; i++) {
                    nextX += potentialMove.dx;
                    nextY += potentialMove.dy;

                    int token = get(nextX, nextY);
                    // Bloqué par ennemi sur le trajet?
                    if (token != 0 && tokenFrom != token) {
                        possible = false;
                    }
                }

                // Bloqué par soi même à la destination
                if (tokenFrom == get(to.getX(), to.getY())) {
                    possible = false;
                }

                if (possible) {
                    validMoves.add(to);
                }
            }
        }

        return validMoves;
    }

    public boolean validMove(Square from, Square to) {
        ArrayList<Square> validMoves = validMoves(from);
        return validMoves.contains(to);
    }

    public int get(int x, int y) {
        return board[x][y];
    }

    private void set(int x, int y, int value) {
        board[x][y] = value;
    }

    public Board clone() {
        int[][] copyBoard = new int[8][8];

        for(int y = 0; y < 8; y++) {
            copyBoard[y] = Arrays.copyOf(board[y], 8);
        }

        int[] copyLineTokens = Arrays.copyOf(lineCount, 8);
        int[] copyColumnTokens = Arrays.copyOf(columnCount, 8);
        int[] copyDiagonal1Tokens = Arrays.copyOf(diagonalRightCount, 15);
        int[] copyDiagonal2Tokens = Arrays.copyOf(diagonalLeftCount, 15);

        return new Board(copyBoard, copyLineTokens, copyColumnTokens, copyDiagonal1Tokens, copyDiagonal2Tokens);
    }

    abstract class P {
        public final int dx;
        public final int dy;

        public P( int dx, int dy){
            this.dx = dx;
            this.dy = dy;
        }

        abstract int tokenCount(int x, int y);
    }

    class PV extends P {
        public PV( int dx, int dy) {super(dx, dy);}

        public int tokenCount(int x, int y) {
            return columnCount[x];
        }
    }

    class PH extends P {
        public PH( int dx, int dy) {super(dx, dy);}

        public int tokenCount(int x, int y) {
            return lineCount[y];
        }
    }

    class PDL extends P {
        public PDL( int dx, int dy) {super(dx, dy);}

        public int tokenCount(int x, int y) {
            return diagonalLeftCount[(14 - x - y)];
        }
    }

    class PDR extends P {
        public PDR( int dx, int dy) {super(dx, dy);}

        public int tokenCount(int x, int y) {
            return diagonalRightCount[(x - y + 7)];
        }
    }
}
