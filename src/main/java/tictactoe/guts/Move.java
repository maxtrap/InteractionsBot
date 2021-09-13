package tictactoe.guts;

public record Move(CellEntry move, GridPosition position) {

    public int row() {
        return position.row();
    }

    public int col() {
        return position.col();
    }

    public CellEntry nextTurn() {
        return TicTacToeGame.nextTurn(move);
    }

}
