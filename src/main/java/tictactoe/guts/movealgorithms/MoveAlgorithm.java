package tictactoe.guts.movealgorithms;

import tictactoe.guts.gameactions.Move;
import tictactoe.guts.gamestate.CellEntry;
import tictactoe.guts.gamestate.GameBoard;

public interface MoveAlgorithm {

    Move getMove(CellEntry turn, GameBoard board);

}
