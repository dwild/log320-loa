package net.dwild.ets.log320.ClientData;

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
        else if ((otherSquare.getX() == x - 1 || otherSquare.getX() == x || otherSquare.getX() == x + 1)
                && (otherSquare.getY() == y - 1 || otherSquare.getY() == y || otherSquare.getY() == y + 1)) {
            adj = true;
        }
        return adj;
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
