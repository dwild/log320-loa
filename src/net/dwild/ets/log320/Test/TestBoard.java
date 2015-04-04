package net.dwild.ets.log320.Test;

import net.dwild.ets.log320.ClientData.TurnPlay;
import net.dwild.ets.log320.GameLogic.Chunk;
import org.junit.*;
import static org.junit.Assert.* ;

import net.dwild.ets.log320.ClientData.ClientPlayer;
import net.dwild.ets.log320.Server.IProtocol;
import net.dwild.ets.log320.Server.ProtocolTCP;
import net.dwild.ets.log320.GameLogic.Board;
import net.dwild.ets.log320.ClientData.Square;
import java.util.ArrayList;


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
        board.createChunks();
        assertEquals("Test initial black", 2, board.getChunkSize(Board.BLACK));
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
        board.createChunks();
        assertEquals("Test initial white", 2, board.getChunkSize(Board.WHITE));
        assertEquals("Test simple final black", 1, board.getChunkSize(Board.BLACK));
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
        board.createChunks();
        assertEquals("Test complex final black", 1, board.getChunkSize(Board.BLACK));
    }

    @Test
    public void testMove(){
        IProtocol connexionTCP = new ProtocolTCP("localhost", 8888);
        ClientPlayer client = new ClientPlayer(connexionTCP);
        Board board = client.createBoard("" +
                "0 0 0 0 0 0 0 0 " +
                "4 0 0 2 0 0 0 4 " +
                "4 0 2 0 2 2 0 4 " +
                "4 0 0 2 2 2 0 4 " +
                "4 0 2 2 2 2 0 4 " +
                "4 0 0 0 0 0 0 4 " +
                "4 0 0 0 0 0 2 4 " +
                "0 0 0 0 0 0 0 0");
        board.createChunks();
        assertEquals("Test fragmented black", 2, board.getChunkSize(Board.BLACK));

        board.move(new TurnPlay(new Square("G","2"), new Square("G","3")));

        assertEquals("Test moved final black", 1, board.getChunkSize(Board.BLACK));

        board.move(new TurnPlay(new Square("G","3"), new Square("G","2")));

        assertEquals("Test removed fragmented black", 2, board.getChunkSize(Board.BLACK));
        
        board.move(new TurnPlay(new Square("G","2"), new Square("H","3")));

        assertEquals("Test eat fragmented black", 2, board.getChunkSize(Board.BLACK));
        assertEquals("Test eat fragmented white", 3, board.getChunkSize(Board.WHITE));
    }

}
