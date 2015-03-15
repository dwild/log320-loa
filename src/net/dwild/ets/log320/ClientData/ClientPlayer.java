package net.dwild.ets.log320.ClientData;

import net.dwild.ets.log320.GameLogic.Board;
import net.dwild.ets.log320.Server.IProtocol;


public class ClientPlayer {
    private IProtocol protocol;
    private Board board;

    public ClientPlayer(IProtocol protocol) {
        this.protocol = protocol;
    }

    public void initConnexion() {
        protocol.connect();
    }

    public boolean isConnected() {
        return protocol.isConnected();
    }

    public void end() {
        protocol.disconnect();
    }

    public char getCommand() {
        char cmd = protocol.readCMD();
        return cmd;
    }

    public Board createBoard() {
        String answer = protocol.readLine(1024);

        int[][] boardArray = new int[8][8];
        String[] boardValues;

        boardValues = answer.split(" ");

        int x = 0, y = 0;
        for (int i = 0; i < boardValues.length; i++) {
            boardArray[x][y] = Integer.parseInt(boardValues[i]);

            x++;
            if (x == 8) {
                x = 0;
                y++;
            }
        }

        Board board = new Board(boardArray);

        this.board = board;

        return board;
    }

    public TurnPlay getTurnOpponent() {
	String answer = protocol.readLine(16);
	answer = answer.replaceAll("\\s|-","");
		
	String from = answer.substring(0, 2);
	String to = answer.substring(2);
		
	return new TurnPlay(new Square(from), new Square(to));	
    }


    public void sendTurn(TurnPlay turn) {
        protocol.send(turn.toString());
    }

    public Board getBoard(){
        return this.board;
    }
}
