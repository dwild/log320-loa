package net.dwild.ets.log320.GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

import net.dwild.ets.log320.ClientData.ClientPlayer;
import net.dwild.ets.log320.ClientData.Square;
import net.dwild.ets.log320.ClientData.TurnPlay;
import net.dwild.ets.log320.Interface.CommandLineInterface;

public class Game {

    char START_WHITE = '1';
    char START_BLACK = '2';
    char MOVE_RECEIVED = '3';
    char INVALID_MOVE = '4';

    int CASE_VIDE = 0;

    private ClientPlayer client;
    private CommandLineInterface commandLineInterface;
    private Board board;
    private int color;
    private int opponentColor;
    private double playerConnectivity;
    private double opponentConnectivity;
    // À RETIRER***********************
    private BufferedReader console;

    public Game(ClientPlayer client) {
        board = new Board();

        this.client = client;
        // À RETIRER***********************
        console = new BufferedReader(new InputStreamReader(System.in));
        commandLineInterface = new CommandLineInterface();
    }

    // Version super préliminaires
    public double evaluate(Board aBoard, int playerColor){
        double value;

        if (playerColor == color){
            value = -aBoard.averageDistance(color);
        }
        else {
            value = aBoard.averageDistance(opponentColor);
        }

        return value;
    }

    public double minMax(Board aBoard, TurnPlay move, int playerColor, double alpha, double beta, int iteration) {

        aBoard.move(move);

        Double value = evaluate(aBoard, playerColor);
        if ((int)aBoard.checkConnectivity(color) == 1 || (int)aBoard.checkConnectivity(opponentColor) == 1) {
            return value;
        }
        if (iteration >= 4){
            return value;
        }

        // MAX
        if (playerColor == color){
            ArrayList<TurnPlay> valid_moves = aBoard.allPossibleMoves(color);
            double maxScore = -2000.00;
            for (TurnPlay turn:valid_moves){
                Double score = minMax(aBoard.clone(), turn, opponentColor, Double.max(alpha, maxScore), beta, iteration+1);
                maxScore = Double.max(maxScore, score);
                if (maxScore >= beta){
                    return maxScore;
                }
            }
            return maxScore;
        }

        // MIN
        if (playerColor == opponentColor){
            ArrayList<TurnPlay> valid_moves = aBoard.allPossibleMoves(opponentColor);
            Double minScore = 2000.00;
            for (TurnPlay turn:valid_moves){
                Double score = minMax(aBoard.clone(), turn, color, alpha, Double.min(beta, minScore), iteration+1);
                minScore = Double.min(minScore, score);
                if (minScore <= alpha){
                    return minScore;
                }
            }
            return minScore;
        }
        return 0;
    }

    public void play() {
        client.initConnexion();
        while (client.isConnected()) {
            char cmd = client.getCommand();
            if (cmd == MOVE_RECEIVED) {
                TurnPlay turnOpponent = client.getTurnOpponent();
                alterBoard(turnOpponent);
                drawTurn(turnOpponent, opponentColor);
                playTurn();
            } else if (cmd == INVALID_MOVE) {
                System.out.println("Coup refusé par le serveur.");
                throw new IllegalStateException("invalid move");
                // L'application produit des illegal moves, parfois. À corriger!
                //playTurn();
            } else if (cmd == START_WHITE) {
                this.color = Board.WHITE;
                this.opponentColor = Board.BLACK;
                board = client.createBoard();
                commandLineInterface.drawBoard(board);
                playTurn();
            } else if (cmd == START_BLACK) {
                this.color = Board.BLACK;
                this.opponentColor = Board.WHITE;
                board = client.createBoard();
                commandLineInterface.drawBoard(board);
            }
        }
    }

    public void playTurn() {

        // Temporaire, juste pour faire "jouer" avec le serveur
        ArrayList<TurnPlay> valid_moves = client.getBoard().allPossibleMoves(color);

        playerConnectivity = board.checkConnectivity(color);
        opponentConnectivity = board.checkConnectivity(opponentColor);

        Random randomGenerator = new Random();
        int i = randomGenerator.nextInt(valid_moves.size());
        Double maxScore = 0.00;
        for (int j=0 ; j<valid_moves.size(); j++){
            Board newBoard = board.clone();

            double value = minMax(newBoard, valid_moves.get(j), opponentColor, -200, 200, 0);
            if (value > maxScore){
                maxScore = value;
                i = j;
            }
        }
        TurnPlay turn = valid_moves.get(i);
        client.sendTurn(turn);
        alterBoard(turn);
        drawTurn(turn, color);
    }

    // Ici, la connectivity est indiquée pour le débug. Clairement à enlever pour la performance
    public void drawTurn(TurnPlay move, int playerColor){
        String message;
        if (playerColor == Board.WHITE) {
           message =  "Les blancs jouent " + move + "\nConnectivity : " + board.checkConnectivity(Board.WHITE)
                   + "\nAvg Dist : " + board.averageDistance(Board.WHITE);
        }
        else {
            message =  "Les noirs jouent " + move + "\nConnectivity : " + board.checkConnectivity(Board.BLACK)
                    + "\nAvg Dist : " + board.averageDistance(Board.BLACK);
        }
        commandLineInterface.drawBoard(board);
        System.out.println(message);
    }

    public void endTurn(TurnPlay move) {
        board.move(move);
        commandLineInterface.drawBoard(board);
        client.sendTurn(move);
    }

    public void alterBoard(TurnPlay turnOpponent) {
        board.move(turnOpponent);
    }

    public void showInterface() {
        // show interface here
    }

    // À RETIRER***********************
    public void getInputConsoleMove() {
        String move = null;
        try {
            move = console.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TurnPlay turn = null;

        if (move != null) {
            move = move.replaceAll("\\s", "");
            int indexOfHyphen = move.indexOf("-");
            String from = move.substring(0, indexOfHyphen);
            String to = move.substring(indexOfHyphen + 1);

            turn = new TurnPlay(new Square(from), new Square(to));
        }

        if (turn == null) {
            System.out.println("Déplacement invalide!");
            getInputConsoleMove();
        } else {
            ArrayList<Square> validMove = board.validMoves(turn.getFrom());
            if (!validMove.contains(turn.getTo())) {
                System.out.println("Déplacement invalide!");

                for (Square possibleMove : validMove) {
                    System.out.println(possibleMove);
                }

                getInputConsoleMove();
            } else {
                endTurn(turn);
            }
        }
    }
}
