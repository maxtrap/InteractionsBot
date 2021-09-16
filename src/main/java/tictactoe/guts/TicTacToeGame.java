package tictactoe.guts;

import tictactoe.guts.gameactions.Move;
import tictactoe.guts.gamestate.CellEntry;
import tictactoe.guts.gamestate.GameBoard;
import tictactoe.guts.gamestate.GameEnd;
import tictactoe.guts.gamestate.GridPosition;
import tictactoe.guts.movealgorithms.*;

public class TicTacToeGame {

    public static final int GRID_SIZE = 3; //The size of tic-tac-toe grid. Standard is 3x3, which is why this value should be 3
    public static final CellEntry START_MOVE = CellEntry.X;

    private final GameBoard gameboard;
    private CellEntry turn;
    private MoveAlgorithm moveAlgorithm;

    public TicTacToeGame() {
        gameboard = new GameBoard();
    }

    public TicTacToeGame startGame(MoveAlgorithmOption algorithm) {
        turn = START_MOVE;
        moveAlgorithm = switch (algorithm) {
            case EASY -> new EasyAlgorithm();
            case MEDIUM -> new MediumAlgorithm();
            case IMPOSSIBLE -> new ImpossibleAlgorithm();
        };
        return this;
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
        Move compMove = moveAlgorithm.getMove(turn, gameboard);
        switchTurns();
        return compMove;
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
