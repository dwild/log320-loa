package net.dwild.ets.log320.GameLogic;


import net.dwild.ets.log320.ClientData.Square;

import java.util.ArrayList;
import java.util.HashSet;

public class ChunkSet {

    private ArrayList<Chunk> chunks = new ArrayList<Chunk>();

    public ChunkSet(ArrayList<Square> content){
        chunks.addAll(splitChunk(new Chunk(content)));
    }

    private ChunkSet(ArrayList<Chunk> chunks, boolean clone){
        if (clone){
            this.chunks = chunks;
        }
    }

    public Chunk findChunkContaining(Square square){
        for (Chunk chunk:chunks){
            if (chunk.contains(square)){
                return chunk;
            }
        }
        return null;
    }

    public void move(Square from, Square to){
        Chunk chunk = findChunkContaining(from);
        chunk.removeSquare(from);
        update(chunk);
        chunks.add(new Chunk(to));
    }

    public boolean checkCollision(Square square){
        Chunk collision = findChunkContaining(square);
        if (collision!=null){
            collision.removeSquare(square);
            update(collision);
            return true;
        }
        return false;
    }

    private void update(Chunk chunk){
        if (chunk.size()==0){
            chunks.remove(chunk);
        }
        else{
            ArrayList<Chunk> chunkList = splitChunk(chunk);
            if (chunkList.size() > 1){
                chunks.remove(chunk);
                chunks.addAll(chunkList);
            }
        }


    }

    public ArrayList<Chunk> splitChunk(Chunk chunk){
        return splitChunk(chunk, false);
    }

    public ArrayList<Chunk> splitChunk(Chunk chunk, boolean verbose){
        ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
        ArrayList<Square> isolated = chunk.getIsolated();
        chunkList.add(chunk);
        while (isolated.size() > 0){
            Chunk newChunk = new Chunk(isolated);
            isolated = newChunk.getIsolated();
            chunkList.add(newChunk);
        }
        return chunkList;
    }


    public void checkChunkProximity(){
        boolean running = true;
        Chunk chunk1;
        Chunk chunk2;
        while (running){
            chunk1 = null;
            chunk2 = null;
            for (Chunk ch1:chunks){
                for (Chunk ch2:chunks){
                    if (ch1.isAdjacent(ch2)){
                        chunk1 = ch1;
                        chunk2 = ch2;
                        break;
                    }
                }
            }
            if (chunk1!=null){
                mergeChunks(chunk1, chunk2);
            }
            else {
                running = false;
            }
        }
    }

    private void mergeChunks(Chunk ch1, Chunk ch2){
        ArrayList<Square> squares = new ArrayList<Square>();
        HashSet<Square> proximity = new HashSet<Square>();
        squares.addAll(ch1.getContent());
        squares.addAll(ch2.getContent());
        proximity.addAll(ch1.getProximity());
        proximity.addAll(ch2.getProximity());
        chunks.remove(ch1);
        chunks.remove(ch2);
        chunks.add(new Chunk(squares, proximity));
    }

    public int size(){
        return chunks.size();
    }

    public ChunkSet clone(){
        ArrayList<Chunk> newChunks = new ArrayList<Chunk>();
        for (Chunk ch:chunks){
            newChunks.add(ch.clone());
        }
        return new ChunkSet(newChunks, true);
    }

    public int getLarger(){
        int max = 0;
        for (Chunk ch:chunks){
            if (ch.size() > max) {
                max = ch.size();
            }
        }
        return max;
    }
}
