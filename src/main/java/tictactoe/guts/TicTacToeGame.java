package tictactoe.guts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicTacToeGame {

    public static final int GRID_SIZE = 3; //The size of tic-tac-toe grid. Standard is 3x3, which is why this value should be 3
    public static final CellEntry START_MOVE = CellEntry.X;

    private final GameBoard gameboard;
    private CellEntry turn;

    TicTacToeGame() {
        gameboard = new GameBoard();
        turn = START_MOVE;
    }

    public boolean isMoveValid(int gridPositionId) {
        GridPosition position = GridPosition.getGridPositionFromId(gridPositionId);
        return gameboard.getEntry(position.row(), position.col()) == CellEntry.EMPTY;
    }

    public Move playerMove(int gridPositionId) {
        GridPosition position = GridPosition.getGridPositionFromId(gridPositionId);
        gameboard.move(turn, position.row(), position.col());
        switchTurns();
        return new Move(turn, gameboard);
    }

    public Move computerMove() {
        List<Integer> possibleMoves = IntStream.range(0, TicTacToeGame.GRID_SIZE * TicTacToeGame.GRID_SIZE)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        GridPosition move;
        int index;
        do {
            if (possibleMoves.size() == 0)
                throw new IllegalStateException("Computer cannot move as there are no possible moves");
            index = (int) (Math.random() * possibleMoves.size());
            move = GridPosition.getGridPositionFromId(possibleMoves.get(index));
            possibleMoves.remove(index);
        } while (gameboard.getEntry(move) != CellEntry.EMPTY);

        gameboard.move(turn, move.row(), move.col());
        switchTurns();
        return new Move(turn, gameboard);
    }

    public GameBoard getGameboard() {
        return gameboard;
    }

    public GameEnd getGameEnd() {
        return gameboard.getGameEnd(nextTurn(turn));
    }

    private void switchTurns() {
        turn = nextTurn(turn);
    }


    static CellEntry nextTurn(CellEntry currentTurn) {
        return currentTurn == CellEntry.X ? CellEntry.O : CellEntry.X;
    }

}
