package ClientData;

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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		StringBuilder str = new StringBuilder("");
		str.append(horizontalIndexes.charAt(x));
		str.append(verticalIndexes.charAt(y));
		return str.toString();
	}
}
