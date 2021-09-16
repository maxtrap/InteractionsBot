package tictactoe.guts.gamestate;

import tictactoe.guts.TicTacToeGame;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record GameEnd(GameBoard gameBoard, boolean isDraw, CellEntry winner, Set<Integer> winRowIds) {

    static GameEnd getGameEnd(GameBoard gameBoard, CellEntry turn) {
        CellEntry[][] board = gameBoard.getGameboardArray();

        //Check rows
        for (int row = 0; row < TicTacToeGame.GRID_SIZE; row++) {
            if (Arrays.stream(board[row]).allMatch(entry -> entry == turn)) {
                return new GameEnd(gameBoard, false, turn, getRowIds(row));
            }
        }

        //Check columns
        checkColumns:
        for (int col = 0; col < TicTacToeGame.GRID_SIZE; col++) {
            for (int row = 0; row < TicTacToeGame.GRID_SIZE; row++) {
                if (board[row][col] != turn)
                    continue checkColumns;
            }
            return new GameEnd(gameBoard, false, turn, getColIds(col));
        }

        //Check main diagonal
        if (IntStream.range(0, TicTacToeGame.GRID_SIZE)
                .mapToObj(i -> board[i][i])
                .allMatch(entry -> entry == turn)) {

            return new GameEnd(gameBoard, false, turn,
                    IntStream.range(0, TicTacToeGame.GRID_SIZE)
                            .map(id -> id * (TicTacToeGame.GRID_SIZE + 1))
                            .boxed()
                            .collect(Collectors.toSet())
            );
        }

        //Check anti-diagonal
        if (IntStream.range(0, TicTacToeGame.GRID_SIZE)
                .mapToObj(i -> board[i][TicTacToeGame.GRID_SIZE - 1 - i])
                .allMatch(entry -> entry == turn)) {

            return new GameEnd(gameBoard, false, turn,
                    IntStream.rangeClosed(1, TicTacToeGame.GRID_SIZE)
                            .map(id -> id * TicTacToeGame.GRID_SIZE - id)
                            .boxed()
                            .collect(Collectors.toSet())
            );
        }

        //Check draw
        if (Arrays.stream(board).flatMap(Arrays::stream).noneMatch(entry -> entry == CellEntry.EMPTY)) {
            return new GameEnd(gameBoard, true, null, null);
        }

        return null;
    }

    private static Set<Integer> getRowIds(int row) {
        return IntStream
                .range(row * TicTacToeGame.GRID_SIZE, row * TicTacToeGame.GRID_SIZE + TicTacToeGame.GRID_SIZE)
                .boxed()
                .collect(Collectors.toSet());
    }

    private static Set<Integer> getColIds(int col) {
        return IntStream
                .range(0, TicTacToeGame.GRID_SIZE)
                .map(id -> id * 3 + col)
                .boxed()
                .collect(Collectors.toSet());
    }
}
