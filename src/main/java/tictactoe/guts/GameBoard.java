package tictactoe.guts;

import java.util.Arrays;

public class GameBoard {

    private final CellEntry[][] gameboard;

    public GameBoard() {
        gameboard = new CellEntry[TicTacToeGame.GRID_SIZE][TicTacToeGame.GRID_SIZE];
        for (CellEntry[] entries : gameboard) {
            Arrays.fill(entries, CellEntry.EMPTY);
        }
    }

    public void move(CellEntry move, int row, int col) {
        if (move == CellEntry.EMPTY)
            throw new IllegalArgumentException("Must be a valid move. Move can't be empty since there is no undo moves.");

        gameboard[row][col] = move;
    }



    public CellEntry getEntry(int row, int col) {
        return gameboard[row][col];
    }

    GameEnd getGameEnd(CellEntry turn) {
        return GameEnd.getGameEnd(gameboard, turn);
    }
}
