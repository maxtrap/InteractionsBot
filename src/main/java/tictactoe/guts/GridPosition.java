package tictactoe.guts;

public record GridPosition(int row, int col) {

    //Method that creates the position instance based on the id.
    public static GridPosition getGridPositionFromId(String id) {
        return getGridPositionFromId(Integer.parseInt(id));
    }

    public static GridPosition getGridPositionFromId(int id) {
        return new GridPosition(id / TicTacToeGame.GRID_SIZE, id % TicTacToeGame.GRID_SIZE);
    }

    //Methods that allows each cell in the tic-tac-toe grid to have its own unique identifier
    public static String getGridPositionId(int row, int col) {
        return String.valueOf(row * TicTacToeGame.GRID_SIZE + col);
    }

}
