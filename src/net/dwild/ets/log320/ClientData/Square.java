package net.dwild.ets.log320.ClientData;

import java.lang.Math;
import java.util.ArrayList;

public class Square {
    private String horizontalIndexes = "ABCDEFGH";
    private String verticalIndexes = "87654321";
    private int x;
    private int y;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Square(String x, String y) {
        buildSquare(x, y);
    }

    public Square(String move) {
        buildSquare(move.substring(0, 1), move.substring(1));
    }

    private void buildSquare(String x, String y) {
        this.x = horizontalIndexes.indexOf(x);
        this.y = verticalIndexes.indexOf(y);
    }

    public boolean isAdjacent(Square otherSquare) {
        boolean adj = false;
        if (otherSquare.equals(this)){
            return false;
        }
        else if ((otherSquare.getX() == this.x-1 || otherSquare.getX() == this.x || otherSquare.getX() == this.x+1)
                && (otherSquare.getY() == this.y-1 || otherSquare.getY() == this.y || otherSquare.getY() == this.y+1)) {
            adj = true;
        }
        return adj;
    }

    public double distanceTo(Square otherSquare) {
        double distanceX = Math.abs(x - otherSquare.getX());
        double distanceY = Math.abs(y - otherSquare.getY());

        return Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));
    }

    public ArrayList<Square> getAdjacents(){
        ArrayList<Square> tempList = new ArrayList<Square>();
        tempList.add(new Square(x - 1, y - 1));
        tempList.add(new Square(x, y - 1));
        tempList.add(new Square(x + 1, y - 1));
        tempList.add(new Square(x - 1, y));
        tempList.add(new Square(x + 1, y));
        tempList.add(new Square(x - 1, y + 1));
        tempList.add(new Square(x, y + 1));
        tempList.add(new Square(x + 1, y + 1));
        ArrayList<Square> squareList = new ArrayList<Square>();
        for (Square square:tempList){
            if (square.isValid()){
                squareList.add(square);
            }
        }
        return squareList;
    }

    private boolean isValid(){
        if (x<0 || x>7 || y<0 || y>7){
            return false;
        }
        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        if (x != square.x) return false;
        if (y != square.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("");
        str.append(horizontalIndexes.charAt(x));
        str.append(verticalIndexes.charAt(y));
        return str.toString();
    }
}
