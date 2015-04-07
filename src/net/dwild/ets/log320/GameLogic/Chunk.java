package net.dwild.ets.log320.GameLogic;

import net.dwild.ets.log320.ClientData.Square;
import java.util.ArrayList;
import java.util.HashSet;

public class Chunk {

    private ArrayList<Square> content;
    private HashSet<Square> proximity = new HashSet<Square>();
    //private ArrayList<Square> proximity = new ArrayList<Square>();

    public Chunk(ArrayList<Square> content){
        this.content = content;
        updateProximity();
    }

    public Chunk(Square square){
        this.content = new ArrayList<Square>();
        content.add(square);
        updateProximity();
    }

    public Chunk(ArrayList<Square> content, HashSet<Square> proximity){
        this.content = content;
        this.proximity = proximity;
    }

    public ArrayList<Square> getIsolated(){
        return getIsolated(false);
    }


    public ArrayList<Square> getIsolated(boolean verbose){
        ArrayList<Square> isolated = new ArrayList<Square>();
        ArrayList<Square> connected = new ArrayList<Square>();
        isolated.addAll(content);
        Square first = content.get(0);
        isolated.remove(first);
        connected.add(first);
        if (verbose) {
            System.out.println("Starting with " + first);
        }
        // La double boucle est pas cool, mais nécessaire, même si sq1 n'est jamais utilisé directement.
        // Si on passe une seule fois, il peut arriver (il VA arriver) des fois où une case n'est adjacente avec aucune
        // des cases déjà retenues, mais adjacente avec de futures cases adjacentes. Ça aurait comme effet de créer des
        // Chunks incomplets. Of course, ça peut être rattrapé par l'étape de fusion, qui vient plus tard.
        for (Square sq1:content){
            for (Square sq2 : content) {
                if (!connected.contains(sq2)) {
                    if (isConnectedTo(sq2, connected)) {
                        if (verbose) {
                            System.out.println(sq2 + " is connected to the chunk.");
                        }
                        connected.add(sq2);
                        isolated.remove(sq2);
                    }
                }
            }
        }

        if (!isolated.isEmpty()) {
            removeFromProximity(isolated);
            for (Square square : isolated) {
                content.remove(square);
            }
        }
        return isolated;
    }

    public void updateProximity(){
        proximity.clear();
        for (Square square:content){
            proximity.addAll(square.getAdjacents());
        }
    }

    public void removeFromProximity(ArrayList<Square> isolated){
        for (Square square:content){
            proximity.removeAll(square.getAdjacents());
        }
    }

    public HashSet<Square> getProximity(){
        return proximity;
    }

    public ArrayList<Square> getContent(){
        return content;
    }

    private boolean isConnectedTo(Square square, ArrayList<Square> connected){
        boolean result = false;
        for (Square sq:connected){
            if (square.isAdjacent(sq)){
                result = true;
                break;
            }
        }
        return result;
    }


    public boolean contains(Square square){
        return content.contains(square);
    }

    public void removeSquare(Square from){
        content.remove(from);
        updateProximity();
    }

    public int size(){
        return content.size();
    }

    // Cette méthode va chercher l'intersection entre le proximity de ce Chunk et
    // la liste des Square constituant l'autre Chunk. S'il y a une intersection, les deux
    // chunk devront être mergés
    public boolean isAdjacent(Chunk otherChunk){
        if (this.equals(otherChunk)){
            return false;
        }
        HashSet<Square> intersection = new HashSet<Square>(proximity);
        intersection.retainAll(otherChunk.getContent());
        if (!intersection.isEmpty()){
            return true;
        }
        return false;
    }

    public boolean equals(Chunk otherChunk){
        if (this.content == otherChunk.content){
            return true;
        }
        return false;
    }

    public Chunk clone(){
        ArrayList<Square> newSquareList = new ArrayList<Square>();
        newSquareList.addAll(content);
        HashSet<Square> newProximity = new HashSet<Square>();
        newProximity.addAll(proximity);
        return new Chunk(newSquareList, newProximity);
    }

}
