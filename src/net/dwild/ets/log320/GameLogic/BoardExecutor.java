package net.dwild.ets.log320.GameLogic;

import net.dwild.ets.log320.ClientData.TurnPlay;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoardExecutor {
    private BoardNode currentNode;

    private ExecutorService executor;

    private int color;
    private int opponentColor;

    public BoardExecutor(Board currentBoard, int color, int opponentColor, boolean maximizingPlayer) {
        this.color = color;
        this.opponentColor = opponentColor;

        currentNode = new BoardNode(currentBoard, maximizingPlayer);
        currentNode.generateChildrens(color, opponentColor);
        executor = Executors.newFixedThreadPool(currentNode.getChildrens().size());
    }

    public void changeCurrentNode(TurnPlay move) {
        executor.shutdownNow();

        for(BoardNode board:currentNode.getChildrens()) {
            if(move.equals(board.getLastMove())) {
                board.nullParent();

                currentNode = board;
                currentNode.generateChildrens(color, opponentColor);
                break;
            }
        }
    }

    public void execute(int depth, long maxTime) {
        currentNode.generateChildrens(color, opponentColor);
        System.out.println("Thread pool: " + currentNode.getChildrens().size());
        executor = Executors.newFixedThreadPool(currentNode.getChildrens().size());
        for(BoardNode board:currentNode.getChildrens()) {
            executor.execute(new BoardNodeWorkerThread(board, depth - 1, maxTime, color, opponentColor));
        }
    }

    public BoardNode getCurrentNode() {
        return currentNode;
    }

    public class BoardNodeWorkerThread implements Runnable {

        public BoardNode boardNode;
        public int depth;
        public long maxTime;

        private int color;
        private int opponentColor;

        public BoardNodeWorkerThread(BoardNode boardNode, int depth, long maxTime, int color, int opponentColor){
            this.boardNode = boardNode;
            this.depth = depth;
            this.maxTime = maxTime;
            this.color = color;
            this.opponentColor = opponentColor;
        }

        @Override
        public void run() {
            double score = alphabeta(boardNode.getCurrentBoard(), depth, maxTime, -Double.MAX_VALUE, Double.MAX_VALUE, false);
            boardNode.setScore(score);
            boardNode.getParent().notifyScore(score, boardNode);

            System.out.println("T: " + (maxTime - System.currentTimeMillis()) + " Move: " + boardNode.getLastMove() + " Score: " + score);
        }

        public double alphabeta(Board board, int depth, long maxTime, double alpha, double beta, Boolean maximizingPlayer) {
            if(board.getChunkSize(color) == 1) {
                return Double.MAX_VALUE;
            }
            else if(board.getChunkSize(opponentColor) == 1) {
                return -Double.MAX_VALUE;
            }
            else if (depth == 0 || System.currentTimeMillis() > maxTime) {
                return board.evaluate(color, opponentColor);
            }

            if (maximizingPlayer) {
                double value = -Double.MAX_VALUE;

                ArrayList<TurnPlay> validMoves = board.allPossibleMoves(color);
                for (TurnPlay turn:validMoves){
                    if(System.currentTimeMillis() > maxTime)
                        break;

                    Board updatedBoard = board.clone();
                    updatedBoard.move(turn);

                    value = Double.max(value, alphabeta(updatedBoard, depth - 1, maxTime, alpha, beta, false));
                    alpha = Double.max(value, alpha);

                    if (beta < alpha) {
                        break;
                    }
                }

                return value;
            }
            else {
                double value = Double.MAX_VALUE;

                ArrayList<TurnPlay> validMoves = board.allPossibleMoves(opponentColor);
                for (TurnPlay turn:validMoves){
                    if(System.currentTimeMillis() > maxTime)
                        break;

                    Board updatedBoard = board.clone();
                    updatedBoard.move(turn);

                    value = Double.min(value, alphabeta(updatedBoard, depth - 1, maxTime, alpha, beta, true));
                    beta = Double.min(beta, value);

                    if (beta < alpha) {
                        break;
                    }
                }

                return value;
            }
        }
    }
}