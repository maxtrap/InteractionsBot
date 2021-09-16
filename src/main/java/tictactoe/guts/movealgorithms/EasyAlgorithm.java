package tictactoe.guts.movealgorithms;

import tictactoe.guts.TicTacToeGame;
import tictactoe.guts.gameactions.Move;
import tictactoe.guts.gamestate.CellEntry;
import tictactoe.guts.gamestate.GameBoard;
import tictactoe.guts.gamestate.GridPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EasyAlgorithm implements MoveAlgorithm {

    @Override
    public Move getMove(CellEntry turn, GameBoard board) {
        List<Integer> possibleMoves = IntStream.range(0, TicTacToeGame.GRID_SIZE * TicTacToeGame.GRID_SIZE)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        GridPosition move;
        int index;
        do {
            if (possibleMoves.size() == 0)
                throw new IllegalStateException("Computer cannot move as there are no possible moves");
            index = (int) (Math.random() * possibleMoves.size());
            move = GridPosition.getGridPositionFromId(possibleMoves.get(index));
            possibleMoves.remove(index);
        } while (board.getEntry(move) != CellEntry.EMPTY);

        board.move(turn, move.row(), move.col());
        return new Move(turn, board);
    }

}
