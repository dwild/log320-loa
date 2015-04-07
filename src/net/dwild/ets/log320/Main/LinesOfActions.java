package net.dwild.ets.log320.Main;

import net.dwild.ets.log320.ClientData.ClientPlayer;
import net.dwild.ets.log320.GameLogic.Game;
import net.dwild.ets.log320.Server.IProtocol;
import net.dwild.ets.log320.Server.ProtocolTCP;


public class LinesOfActions {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8888;

        if(args.length > 0) {
            host = args[0];
        }

        if(args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        IProtocol connexionTCP = new ProtocolTCP(host, port);
        ClientPlayer client = new ClientPlayer(connexionTCP);
        Game game = new Game(client);
        game.play();
        System.out.println("Partie terminé.");
    }
}
