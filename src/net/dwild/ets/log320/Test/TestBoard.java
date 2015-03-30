package net.dwild.ets.log320.Test;

import org.junit.*;
import static org.junit.Assert.* ;

import net.dwild.ets.log320.ClientData.ClientPlayer;
import net.dwild.ets.log320.Server.IProtocol;
import net.dwild.ets.log320.Server.ProtocolTCP;
import net.dwild.ets.log320.GameLogic.Board;

public class TestBoard {

    @Test
    public void testInitial(){
        IProtocol connexionTCP = new ProtocolTCP("localhost", 8888);
        ClientPlayer client = new ClientPlayer(connexionTCP);
        Board board = client.createBoard("" +
                "0 2 2 2 2 2 2 0 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "0 2 2 2 2 2 2 0");
        assertEquals("Test initial black", 0.25, board.checkConnectivity(Board.BLACK), 0.01);
        assertEquals("Test initial white", 0.25, board.checkConnectivity(Board.WHITE), 0.01);
    }

    @Test
    public void testFinal(){
        IProtocol connexionTCP = new ProtocolTCP("localhost", 8888);
        ClientPlayer client = new ClientPlayer(connexionTCP);
        Board board = client.createBoard("" +
                "0 0 0 0 0 0 0 0 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 2 2 2 2 0 4 " +
                "4 0 2 2 2 2 0 4 " +
                "4 0 2 2 2 2 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "0 0 0 0 0 0 0 0");
        assertEquals("Test final black", 1.0, board.checkConnectivity(Board.BLACK), 0.01);
    }

    @Test
    public void testDiagonal(){
        IProtocol connexionTCP = new ProtocolTCP("localhost", 8888);
        ClientPlayer client = new ClientPlayer(connexionTCP);
        Board board = client.createBoard("" +
                "0 0 0 0 0 0 0 0 " +
                "4 0 0 2 0 0 0 4 " +
                "4 0 2 0 2 2 0 4 " +
                "4 0 0 2 2 2 0 4 " +
                "4 0 2 2 2 2 0 4 " +
                "4 0 0 0 0 0 2 4 " +
                "4 0 0 0 0 0 0 4 " +
                "0 0 0 0 0 0 0 0");
        assertEquals("Test diagonal black", 1.0, board.checkConnectivity(Board.BLACK), 0.01);
    }

}
