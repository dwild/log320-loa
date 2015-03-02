package ClientData;


public class TurnPlay {
	
	private Square firstMove;
	private Square secondMove;
	
	public TurnPlay(Square firstMove, Square secondMove) {
		this.firstMove = firstMove;
		this.secondMove = secondMove;
	}
	
	public Square getFirstMove() {
		return firstMove;
	}
	
	public Square getSecondMove() {
		return secondMove;
	}
	
	public String toString() {
		return firstMove.toString() + " - " + secondMove.toString();
	}
}
