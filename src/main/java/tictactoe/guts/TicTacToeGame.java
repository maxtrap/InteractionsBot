package tictactoe.guts;

public class TicTacToeGame {

    public static final int GRID_SIZE = 3; //The size of tic-tac-toe grid. Standard is 3x3, which is why this value should be 3
    public static final CellEntry START_MOVE = CellEntry.X;

    private final GameBoard gameboard;
    private CellEntry turn;

    TicTacToeGame() {
        gameboard = new GameBoard();
        turn = START_MOVE;
    }

    public Move playerMove(int gridPositionId) {
        GridPosition position = GridPosition.getGridPositionFromId(gridPositionId);
        gameboard.move(turn, position.row(), position.col());
        switchTurns();
        return new Move(turn, gameboard);
    }

    public Move computerMove() {
        int r;
        int c;
        do {
            r = (int) (Math.random() * GRID_SIZE);
            c = (int) (Math.random() * GRID_SIZE);
        } while (gameboard.getEntry(r, c) != CellEntry.EMPTY);

        gameboard.move(turn, r, c);
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
