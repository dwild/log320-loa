package net.dwild.ets.log320.GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	private int[][] board = new int[8][8];
	// À RETIRER***********************
	private BufferedReader console;
	
	public Game(ClientPlayer client) {
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
			}
			else if (cmd == INVALID_MOVE) {
				playTurn();
			}
			else if (cmd == START_WHITE) {
				board = client.getBoard();
				commandLineInterface.drawBoard(board);
				playTurn();
			}
			else if (cmd == START_BLACK) {
				board = client.getBoard();
				commandLineInterface.drawBoard(board);
			}
		}
	}
	
	public void playTurn() {
		// some logic to get best move
		
		// À RETIRER***********************
		getInputConsoleMove();
	}
	
	public void endTurn(Square start, Square end) {
		TurnPlay ourTurn = new TurnPlay(start, end);
		client.sendTurn(ourTurn);
	}
	
	public void alterBoard(TurnPlay turnOpponent) {
		// alter board by the most opponent's recent turn
        int storedValue = board[turnOpponent.getFrom().getX()][turnOpponent.getFrom().getY()];
        board[turnOpponent.getFrom().getX()][turnOpponent.getFrom().getY()] = CASE_VIDE;
        board[turnOpponent.getTo().getX()][turnOpponent.getTo().getY()] = storedValue;
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
		
		if (move != null) {
			move = move.replaceAll("\\s","");
			int indexOfHyphen = move.indexOf("-");
			String from = move.substring(0, indexOfHyphen);
			String to = move.substring(indexOfHyphen + 1);
			endTurn(new Square(from), new Square(to));
		}
		else {	
			endTurn(new Square(1, 2), new Square(3, 4));	
		}
	}
}
