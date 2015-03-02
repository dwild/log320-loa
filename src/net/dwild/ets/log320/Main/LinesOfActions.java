package net.dwild.ets.log320.Main;

import net.dwild.ets.log320.ClientData.ClientPlayer;
import net.dwild.ets.log320.GameLogic.Game;
import net.dwild.ets.log320.Server.IProtocol;
import net.dwild.ets.log320.Server.ProtocolTCP;


public class LinesOfActions{
	
	public static void main(String[] args) {
		IProtocol connexionTCP = new ProtocolTCP("localhost", 8888);
		ClientPlayer client = new ClientPlayer(connexionTCP);
		Game game = new Game(client);
		game.play();
		System.out.println("Partie terminé.");
	}	
}
