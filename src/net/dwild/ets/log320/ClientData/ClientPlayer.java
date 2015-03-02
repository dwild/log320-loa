package net.dwild.ets.log320.ClientData;

import net.dwild.ets.log320.Server.IProtocol;


public class ClientPlayer {
	private IProtocol protocol;
	
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
	
	public int[][] getBoard() {
		String answer = protocol.readLine(1024);
		
		int[][] board = new int[8][8];
		String[] boardValues;	
        boardValues = answer.split(" ");
        
        int x = 0, y = 0;
        for(int i = 0; i < boardValues.length; i++){
            board[x][y] = Integer.parseInt(boardValues[i]);
            
            x++;
            if(x == 8){
                x = 0;
                y++;
            }
        }
        
		return board;
	}
	
	public TurnPlay getTurnOpponent() {
		String answer = protocol.readLine(16);
		answer = answer.replaceAll("\\s","");
		
		int indexOfHyphen = answer.indexOf("-");
		String firstMove = answer.substring(0, indexOfHyphen);
		String secondMove = answer.substring(indexOfHyphen + 1);
		
		return new TurnPlay(new Square(firstMove), new Square(secondMove));			
	}

	
	public void sendTurn(TurnPlay turn) {
		protocol.send(turn.toString());
	}
}
