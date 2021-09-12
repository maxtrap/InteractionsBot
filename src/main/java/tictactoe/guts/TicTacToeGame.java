package tictactoe.guts;

import java.util.function.Consumer;

public class TicTacToeGame {

    public static final int GRID_SIZE = 3; //The size of tic-tac-toe grid. Standard is 3x3, which is why this value should be 3

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
        turn = CellEntry.X;
        thenShow.accept(gameboard);
    }

    public void move(GridPosition position, Runnable thenShow) {
        thenShow.run();
    }


    //Methods that allows each cell in the tic-tac-toe grid to have its own unique identifier
    public static String getGridPositionId(int row, int col) {
        return String.valueOf(row * GRID_SIZE + col);
    }

}
