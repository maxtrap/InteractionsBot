package tictactoe.guts.gamestate;

import tictactoe.guts.TicTacToeGame;

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
            throw new IllegalArgumentException("Must be a valid move. Move cannot be empty since there is no undo moves.");
        if (move == null)
            throw new IllegalArgumentException("Move cannot be null");

        gameboard[row][col] = move;
    }

    public CellEntry getEntry(int row, int col) {
        return gameboard[row][col];
    }

    public CellEntry getEntry(GridPosition position) {
        return gameboard[position.row()][position.col()];
    }

    public GameEnd getGameEnd(CellEntry turn) {
        return GameEnd.getGameEnd(this, turn);
    }

    CellEntry[][] getGameboardArray() {
        return gameboard;
    }
}
