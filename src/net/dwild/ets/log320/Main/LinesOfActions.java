package Main;

import ClientData.ClientPlayer;
import GameLogic.Game;
import Server.IProtocol;
import Server.ProtocolTCP;

public class LinesOfActions{
	
	public static void main(String[] args) {
		IProtocol connexionTCP = new ProtocolTCP("localhost", 8888);
		ClientPlayer client = new ClientPlayer(connexionTCP);
		Game game = new Game(client);
		game.play();
		System.out.println("Partie terminé.");   		
	}	
}
