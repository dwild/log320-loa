package net.dwild.ets.log320.GameLogic;

import net.dwild.ets.log320.ClientData.TurnPlay;

import java.util.ArrayList;

public class BoardNode {

    private boolean maximizingPlayer;

    private double score = -Double.MAX_VALUE;

    private TurnPlay lastMove;
    private Board currentBoard;

    private ArrayList<BoardNode> childrens;
    private BoardNode bestNextMove;

    private BoardNode parent;

    public BoardNode(Board currentBoard) {
        this(null, currentBoard, null, true);
    }

    public BoardNode(BoardNode parent, Board currentBoard, TurnPlay lastMove, boolean maximizingPlayer) {
        this.parent = parent;
        this.currentBoard = currentBoard;
        this.lastMove = lastMove;
        this.maximizingPlayer = maximizingPlayer;
    }

    public synchronized void notifyScore(double score, BoardNode children) {
        if(bestNextMove == null || (maximizingPlayer && score > bestNextMove.getScore()) || (!maximizingPlayer && score < bestNextMove.getScore())) {
            this.score = score;
            bestNextMove = children;
        }
    }

    public ArrayList<BoardNode> generateChildrens(int color, int opponentColor) {
        if(this.childrens == null) {
            this.childrens = new ArrayList<BoardNode>();

            ArrayList<TurnPlay> validMoves = new ArrayList<TurnPlay>();
            if(maximizingPlayer) {
                validMoves.addAll(currentBoard.allPossibleMoves(color));
            }
            else {
                validMoves.addAll(currentBoard.allPossibleMoves(opponentColor));
            }

            for(TurnPlay move:validMoves) {
                Board board = currentBoard.clone();
                board.move(move);

                childrens.add(new BoardNode(this, board, move, !maximizingPlayer));
            }
        }

        return this.childrens;
    }

    public void nullParent() {
        this.parent = null;
    }

    public BoardNode getParent() {
        return parent;
    }

    public TurnPlay getLastMove() {
        return lastMove;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public BoardNode getBestNextMove() {
        return bestNextMove;
    }

    public ArrayList<BoardNode> getChildrens() {
        return childrens;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    @Override
    public String toString() {
        return "BoardNode{" +
                "maximizingPlayer=" + maximizingPlayer +
                ", score=" + score +
                ", lastMove=" + lastMove +
                '}';
    }
}
