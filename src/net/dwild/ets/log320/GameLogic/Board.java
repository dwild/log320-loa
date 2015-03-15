package net.dwild.ets.log320.GameLogic;

import net.dwild.ets.log320.ClientData.Square;
import net.dwild.ets.log320.ClientData.TurnPlay;

import java.util.ArrayList;
import java.util.Arrays;

public class Board implements Cloneable {

    public final static int NONE = 0;
    public final static int BLACK = 2;
    public final static int WHITE = 4;

    private int[][] board;

    private int[] lineTokens;
    private int[] columnTokens;
    private int[] diagonal1Tokens;
    private int[] diagonal2Tokens;

    public Board() {
        this(new int[8][8]);
    }

    public Board(int[][] board) {
        this.board = board;

        calcTokens();
    }

    private Board(int[][] board, int[] lineTokens, int[] columnTokens, int[] diagonal1Tokens, int[] diagonal2Tokens) {
        this.board = board;
        this.lineTokens = lineTokens;
        this.columnTokens = columnTokens;
        this.diagonal1Tokens = diagonal1Tokens;
        this.diagonal2Tokens = diagonal2Tokens;
    }

    private void calcTokens() {
        lineTokens = new int[8];
        columnTokens = new int[8];
        diagonal1Tokens = new int[15];
        diagonal2Tokens = new int[15];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) != NONE) {
                    lineTokens[y]++;
                    columnTokens[x]++;
                    diagonal1Tokens[(x - y + 7)]++;
                    diagonal2Tokens[(14 - x - y)]++;
                }
            }
        }
    }

    // Retourne le pourcentage des pions d'une équipe qui sont connectés entre eux.
    // Une connectivité de 1 signifie la victoire.
    public float checkConnectivity(int token){
        ArrayList<Square> positions = new ArrayList<Square>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) == token) {
                    positions.add(new Square(x, y));
                }
            }
        }

        int streak = 0;
        for (Square pos:positions){
            ArrayList<Square> line = new ArrayList<Square>();
            line.add(pos);
            int temp = 0;
            for (Square pos1:positions){
                boolean add = false;
                for (Square pos2:line){
                    if (pos1.isAdjacent(pos2)){
                        add = true;
                        temp++;
                        break;
                    }
                }
                if (add){
                    line.add(pos1);
                }
            }
            if (temp > streak) {
                streak = temp;
            }
        }

        float result = ((float)streak / (float)positions.size());
        return result;
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
                lineTokens[ey]++;
                columnTokens[ex]++;
                diagonal1Tokens[(ex - ey + 7)]++;
                diagonal2Tokens[(14 - ex - ey)]++;
            }

            set(startingPosition.getX(), startingPosition.getY(), NONE);
            set(endingPosition.getX(), endingPosition.getY(), tokenFrom);

            lineTokens[sy]--;
            columnTokens[sx]--;
            diagonal1Tokens[(sx - sy + 7)]--;
            diagonal2Tokens[(14 - sx - sy)]--;
        }
    }



    public ArrayList<TurnPlay> allPossibleMoves(int token) {
        ArrayList<TurnPlay> possibleMoves = new ArrayList<TurnPlay>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) == token) {
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

        int countHorizontal = lineTokens[y];
        int countVertical = columnTokens[x];
        int countDiagonal1 = diagonal1Tokens[(x - y + 7)];
        int countDiagonal2 = diagonal2Tokens[(14 - x - y)];

        validMoves.add(new Square(from.getX() - countDiagonal1, from.getY() - countDiagonal1));
        validMoves.add(new Square(from.getX() + countDiagonal1, from.getY() + countDiagonal1));

        validMoves.add(new Square(from.getX() + countDiagonal2, from.getY() - countDiagonal2));
        validMoves.add(new Square(from.getX() - countDiagonal2, from.getY() + countDiagonal2));

        validMoves.add(new Square(from.getX() - countHorizontal, from.getY()));
        validMoves.add(new Square(from.getX() + countHorizontal, from.getY()));

        validMoves.add(new Square(from.getX(), from.getY() - countVertical));
        validMoves.add(new Square(from.getX(), from.getY() + countVertical));

        ArrayList<Square> finalValidMoves = new ArrayList<Square>();
        for (Square to : validMoves) {
            if (to.getX() > 0 && to.getX() < 8 && to.getY() > 0 && to.getY() < 8 && get(to.getX(), to.getY()) == 0) {
                boolean validMove = true;

                if(Math.abs(from.getX() - to.getX()) > Math.abs(from.getY() - to.getY())) {
                    Square start = (to.getX() > from.getX())?from:to;
                    Square end = (to.getX() < from.getX())?from:to;

                    int dx = end.getX() - start.getX();
                    int dy = end.getY() - start.getY();

                    for(int ix = start.getX(); ix < end.getX(); ix++) {
                        int iy = start.getY() + ( dy * (ix - start.getX())) / dx; //TODO Optimiser ça?

                        int token = get(ix, iy);

                        if(token != NONE && tokenFrom != token) {
                            validMove = false;
                            break;
                        }
                    }
                }
                else {
                    Square start = (to.getY() > from.getY())?from:to;
                    Square end = (to.getY() < from.getY())?from:to;

                    int dx = end.getX() - start.getX();
                    int dy = end.getY() - start.getY();

                    for(int iy = start.getY(); iy < end.getY(); iy++) {
                        int ix = start.getX() + ( dx * (iy - start.getY())) / dy; //TODO Optimiser ça?

                        int token = get(ix, iy);

                        if(token != NONE && tokenFrom != token) {
                            validMove = false;
                            break;
                        }
                    }
                }

                if(validMove) {
                    finalValidMoves.add(to);
                }
            }
        }

        return finalValidMoves;
    }

    public boolean validMove(Square from, Square to) {
        ArrayList<Square> validMoves = validMoves(from);
        return validMoves.contains(to);
    }

    public int get(int x, int y) {
        return board[x][y];
    }

    public void set(int x, int y, int value) {
        board[x][y] = value;
    }

    public Board clone() {
        int[][] copyBoard = new int[8][8];

        for(int y = 0; y < 8; y++) {
            copyBoard[y] = Arrays.copyOf(board[y], 8);
        }

        int[] copyLineTokens = Arrays.copyOf(lineTokens, 8);
        int[] copyColumnTokens = Arrays.copyOf(columnTokens, 8);
        int[] copyDiagonal1Tokens = Arrays.copyOf(diagonal1Tokens, 15);
        int[] copyDiagonal2Tokens = Arrays.copyOf(diagonal2Tokens, 15);

        return new Board(copyBoard, copyLineTokens, copyColumnTokens, copyDiagonal1Tokens, copyDiagonal2Tokens);
    }
}
