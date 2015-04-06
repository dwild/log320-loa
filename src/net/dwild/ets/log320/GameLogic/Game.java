package net.dwild.ets.log320.GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import net.dwild.ets.log320.ClientData.ClientPlayer;
import net.dwild.ets.log320.ClientData.Square;
import net.dwild.ets.log320.ClientData.TurnPlay;
import net.dwild.ets.log320.Interface.CommandLineInterface;

public class Game {

    char START_WHITE = '1';
    char START_BLACK = '2';
    char MOVE_RECEIVED = '3';
    char INVALID_MOVE = '4';

    private ClientPlayer client;
    private CommandLineInterface commandLineInterface;
    private Board board;
    private int color;
    private int opponentColor;

    private long lastTime;

    // À RETIRER***********************
    private BufferedReader console;

    private BoardExecutor boardExecutor;

    public Game(ClientPlayer client) {
        board = new Board();

        this.client = client;
        // À RETIRER***********************
        console = new BufferedReader(new InputStreamReader(System.in));
        commandLineInterface = new CommandLineInterface();
    }
    
    public void play() {
        System.out.println("Press enter to continue...");
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();

        client.initConnexion();
        while (client.isConnected()) {
            char cmd = client.getCommand();
            if (cmd == MOVE_RECEIVED) {
                lastTime = System.currentTimeMillis();

                TurnPlay turnOpponent = client.getTurnOpponent();

                boardExecutor.changeCurrentNode(turnOpponent);

                alterBoard(turnOpponent);
                drawTurn(turnOpponent, opponentColor);
                playTurn();
            } else if (cmd == INVALID_MOVE) {
                System.out.println("Coup refusé par le serveur.");
                throw new IllegalStateException("invalid move");
                // L'application produit des illegal moves, parfois. À corriger!
                //playTurn();
            } else if (cmd == START_WHITE) {
                lastTime = System.currentTimeMillis();

                this.color = Board.WHITE;
                this.opponentColor = Board.BLACK;

                board = client.createBoard("");
                commandLineInterface.drawBoard(board);

                boardExecutor = new BoardExecutor(board, color, opponentColor);

                playTurn();
            } else if (cmd == START_BLACK) {
                lastTime = System.currentTimeMillis();

                this.color = Board.BLACK;
                this.opponentColor = Board.WHITE;

                board = client.createBoard("");
                commandLineInterface.drawBoard(board);

                boardExecutor = new BoardExecutor(board, color, opponentColor);
            }
        }
    }

    public void playTurn() {
        boardExecutor.execute(4, lastTime + 14500);

        try {
            Thread.sleep(14750);
        } catch (Exception e) {}

        BoardNode boardNode = boardExecutor.getCurrentNode().getBestNextMove();
        TurnPlay turn = boardNode.getLastMove();

        System.out.println("Cout : " + turn + " ----- Score : " + boardNode.getScore());

        client.sendTurn(turn);
        boardExecutor.changeCurrentNode(turn);
        alterBoard(turn);
        drawTurn(turn, color);
    }
    
    // Ici, la connectivity est indiquée pour le débug. Clairement à enlever pour la performance
    public void drawTurn(TurnPlay move, int playerColor){
        String message;
        if (playerColor == Board.WHITE) {
           message =  "Les blancs jouent " + move
                   + "\nAvg Dist : " + board.averageDistance(Board.WHITE)
                    + "\nChunks : " + board.getChunkSize(Board.WHITE)
                   + "\nFragmentation : " + board.getFragmentation(Board.WHITE);
        }
        else {
            message =  "Les noirs jouent " + move
                    + "\nAvg Dist : " + board.averageDistance(Board.BLACK)
                    + "\nChunks : " + board.getChunkSize(Board.BLACK)
                    + "\nFragmentation : " + board.getFragmentation(Board.BLACK);
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
