package GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ClientData.ClientPlayer;
import ClientData.Square;
import ClientData.TurnPlay;

public class Game {
	
	private ClientPlayer client;
	private int[][] board = new int[8][8];
	// � RETIRER***********************
	private BufferedReader console;
	
	public Game(ClientPlayer client) {
		this.client = client;
		// � RETIRER***********************
		console = new BufferedReader(new InputStreamReader(System.in)); 
	}
	
	public void play() {
		client.initConnexion();
		while (client.isConnected()) {
			char cmd = client.getCommand();
			
			if (cmd == '3') {
				TurnPlay turnOpponent = client.getTurnOpponent();
				alterBoard(turnOpponent);
				showInterface();
				playTurn();
			}
			else if (cmd == '4') {
				playTurn();
			}
			else if (cmd == '1') {
				board = client.getBoard();
				showInterface();
				playTurn();
			}
			else if (cmd == '2') {
				board = client.getBoard();
				showInterface();
			}
		}
	}
	
	public void playTurn() {
		// some logic to get best move
		
		// � RETIRER***********************
		getInputConsoleMove();
	}
	
	public void endTurn(Square start, Square end) {
		TurnPlay ourTurn = new TurnPlay(start, end);
		client.sendTurn(ourTurn);
	}
	
	public void alterBoard(TurnPlay turnOpponent) {
		// alter board by the most opponent's recent turn
	}
	
	public void showInterface() {
		// show interface here
	}
	
	// � RETIRER***********************
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
			String firstMove = move.substring(0, indexOfHyphen);
			String secondMove = move.substring(indexOfHyphen + 1);
			endTurn(new Square(firstMove), new Square(secondMove));	
		}
		else {	
			endTurn(new Square(1, 2), new Square(3, 4));	
		}
	}
}
