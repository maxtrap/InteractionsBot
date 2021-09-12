package tictactoe.guts;

public record GridPosition(int row, int col) {

    public static GridPosition getGridPositionFromId(String id) {
        int intId = Integer.parseInt(id);
        return new GridPosition(intId / TicTacToeGame.GRID_SIZE, intId % TicTacToeGame.GRID_SIZE);
    }

}
