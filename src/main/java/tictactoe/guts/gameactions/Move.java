package tictactoe.guts.gameactions;

import tictactoe.guts.gamestate.CellEntry;
import tictactoe.guts.gamestate.GameBoard;

public record Move(CellEntry nextTurn, GameBoard gameBoard) {
}
