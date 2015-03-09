package net.dwild.ets.log320.GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
    // À RETIRER***********************
    private BufferedReader console;

    public Game(ClientPlayer client) {
        board = new Board();

        this.client = client;
        // À RETIRER***********************
        console = new BufferedReader(new InputStreamReader(System.in));
        commandLineInterface = new CommandLineInterface();
    }

    public void play() {
        client.initConnexion();
        while (client.isConnected()) {
            char cmd = client.getCommand();
            if (cmd == MOVE_RECEIVED) {
                TurnPlay turnOpponent = client.getTurnOpponent();
                alterBoard(turnOpponent);
                commandLineInterface.drawBoard(board);
                playTurn();
            } else if (cmd == INVALID_MOVE) {
                System.out.println("Coup refusé par le serveur.");
                playTurn();
            } else if (cmd == START_WHITE) {
                board = client.getBoard();
                commandLineInterface.drawBoard(board);
                playTurn();
            } else if (cmd == START_BLACK) {
                board = client.getBoard();
                commandLineInterface.drawBoard(board);
            }
        }
    }

    public void playTurn() {
        // some logic to get best move
        getInputConsoleMove();
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
