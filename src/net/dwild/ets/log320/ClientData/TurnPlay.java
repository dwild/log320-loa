package net.dwild.ets.log320.ClientData;


public class TurnPlay {
    private Square from;
    private Square to;

    public TurnPlay(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurnPlay turnPlay = (TurnPlay) o;

        if (!from.equals(turnPlay.from)) return false;
        return to.equals(turnPlay.to);

    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }

    public String toString() {
        return from.toString() + " - " + to.toString();
    }

}
