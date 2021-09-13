package tictactoe.guts;

public record GridPosition(int row, int col) {

    //Method that creates the position instance based on the id.
    public static GridPosition getGridPositionFromId(String id) {
        int intId = Integer.parseInt(id);
        return new GridPosition(intId / TicTacToeGame.GRID_SIZE, intId % TicTacToeGame.GRID_SIZE);
    }

    //Methods that allows each cell in the tic-tac-toe grid to have its own unique identifier
    public static String getGridPositionId(int row, int col) {
        return String.valueOf(row * TicTacToeGame.GRID_SIZE + col);
    }

}
