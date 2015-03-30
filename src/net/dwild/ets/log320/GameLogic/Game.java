package net.dwild.ets.log320.GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

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
    public double evaluate(Board aBoard){

        //double value = 2*(aBoard.averageDistance(opponentColor))+(6)*aBoard.checkConnectivity(opponentColor);
        //    value -= (aBoard.averageDistance(color))+(6)*aBoard.checkConnectivity(color);
        //
        //return value;

        double value = 0;

        value-= aBoard.averageDistance(color);
        value+= aBoard.checkConnectivity(color) * 40;
        value-= aBoard.checkConnectivity(opponentColor) * 20;
        value+= aBoard.allPossibleMoves(opponentColor).size()/2;

        return value;
    }

    public double maxValue(double nb1, double nb2) {
    	return nb1 > nb2 ? nb1 : nb2;
    }
    
    public double minValue(double nb1, double nb2) {
    	return nb1 < nb2 ? nb1 : nb2;
    }
    
    public double alphabeta(Board board, int depth, double alpha, double beta, Boolean maximizingPlayer) {
    	if (depth == 0) {
            return evaluate(board);
    	}

        if ((int)board.checkFastConnectivity(color) >= 1){
            return Double.MAX_VALUE;
        }
        if ((int)board.checkFastConnectivity(opponentColor) >= 1){
            return -Double.MAX_VALUE;
        }

    	double value;

    	if (maximizingPlayer) {
    		value = -Double.MAX_VALUE;
    		ArrayList<TurnPlay> validMoves = board.allPossibleMoves(color);
    		for (TurnPlay turn:validMoves){
    			Board updatedBoard = board.clone();
    			updatedBoard.move(turn);
    			value = maxValue(value, alphabeta(updatedBoard, depth - 1, alpha, beta, false));
    			alpha = maxValue(alpha, value);
    			if (beta < alpha) {
    				break;
    			}
            }
    		return value;
    	}
    	else {
    		value = Double.MAX_VALUE;
    		ArrayList<TurnPlay> validMoves = board.allPossibleMoves(opponentColor);
    		for (TurnPlay turn:validMoves){
    			Board updatedBoard = board.clone();
    			updatedBoard.move(turn);
    			value = minValue(value, alphabeta(updatedBoard, depth - 1, alpha, beta, true));
    			beta = minValue(beta, value);
    			if (beta < alpha) {
    				break;
    			}
            }
    		return value;
    	}
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
                board = client.createBoard("");
                commandLineInterface.drawBoard(board);
                playTurn();
            } else if (cmd == START_BLACK) {
                this.color = Board.BLACK;
                this.opponentColor = Board.WHITE;
                board = client.createBoard("");
                commandLineInterface.drawBoard(board);
            }
        }
    }

    public void playTurn() {
    	long startTime = System.nanoTime();
        // Temporaire, juste pour faire "jouer" avec le serveur
        ArrayList<TurnPlay> valid_moves = client.getBoard().allPossibleMoves(color);

        playerConnectivity = board.checkConnectivity(color);
        opponentConnectivity = board.checkConnectivity(opponentColor);

        Random randomGenerator = new Random();
        int i = randomGenerator.nextInt(valid_moves.size());
        Double maxScore = 0.00;
        double seconds = 0;
        int j = 0;
        while (j < valid_moves.size() && seconds < 4.8) {
            Board newBoard = board.clone();

            newBoard.move(valid_moves.get(j));
            double value = alphabeta(newBoard, 7, Double.MAX_VALUE, Double.MIN_VALUE, true);

            if (value > maxScore){
                maxScore = value;
                i = j;
            }

            System.out.println("Cout : " + valid_moves.get(j) + " ----- Score : " + value);
            
            j++;
            long solvingTime = System.nanoTime() - startTime;
            seconds = (double)solvingTime / 1000000000.0;
        }
        
        System.out.println("Temps : " + seconds);
        System.out.println("Score : " + maxScore);
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
        message += "\n***********************";
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
