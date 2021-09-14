package tictactoe.guts;

import java.util.function.Consumer;

public class TicTacToeGame {

    public static final int GRID_SIZE = 3; //The size of tic-tac-toe grid. Standard is 3x3, which is why this value should be 3
    public static final CellEntry START_MOVE = CellEntry.X;

    private final GameBoard gameboard;
    private CellEntry turn;

    public TicTacToeGame() {
        gameboard = new GameBoard();
    }

    //The reason I wrote it with functional interfaces is I would rather have the view passed in as a parameter instead of being handled by the caller of the method, because I believe
    //That it is way easier to read on the callers side, as the runnable/consumer is being dealt with AFTER the method is called.
    //The alternative would be to have non-void methods that return the result of an operation such as move, but that would make the code a little less readable
    //on the caller's side.
    public void startGame(Consumer<? super GameBoard> thenShow) {
        turn = START_MOVE;
        thenShow.accept(gameboard);
    }

    public Move playerMove(int gridPositionId) {
        GridPosition position = GridPosition.getGridPositionFromId(gridPositionId);
        gameboard.move(turn, position.row(), position.col());
        Move move = new Move(turn, position);
        switchTurns();
        return move;
    }

    public Move computerMove() {
        int r;
        int c;
        do {
            r = (int) (Math.random() * GRID_SIZE);
            c = (int) (Math.random() * GRID_SIZE);
        } while (gameboard.getEntry(r, c) != CellEntry.EMPTY);

        gameboard.move(turn, r, c);
        Move move = new Move(turn, new GridPosition(r, c));
        switchTurns();
        return move;
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
