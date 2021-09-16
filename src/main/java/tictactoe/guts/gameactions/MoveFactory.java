package tictactoe.guts.gameactions;

import tictactoe.guts.TicTacToeGame;
import tictactoe.guts.gamestate.GameEnd;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class MoveFactory {

    private TicTacToeGame game;
    private final Queue<Showable<?>> moveAndShows;


    public MoveFactory() {
        this(null);
    }

    public MoveFactory(TicTacToeGame game) {
        this.game = game;
        moveAndShows = new ArrayDeque<>();
    }

    public Show<TicTacToeGame> startNewGame() {
        return (Show<TicTacToeGame>) addMoveAndShowToQueue(new Show<>(this, () -> game = new TicTacToeGame()));
    }

    public ShowBreakChain<Void> checkIfValidMove(int gridPositionId) {
        return (ShowBreakChain<Void>) addMoveAndShowToQueue(new ShowBreakChain<Void>(this, () -> null, unused -> !game.isMoveValid(gridPositionId)));
    }

    public Show<Move> playerMove(int gridPositionId) {
        return (Show<Move>) addMoveAndShowToQueue(new Show<>(this, () -> game.playerMove(gridPositionId)));
    }

    public Show<Move> computerMove() {
        return (Show<Move>) addMoveAndShowToQueue(new Show<>(this, game::computerMove));
    }

    public ShowBreakChain<GameEnd> checkGameEnd() {
        return (ShowBreakChain<GameEnd>) addMoveAndShowToQueue(new ShowBreakChain<>(this, game::getGameEnd, Objects::nonNull));
    }



    private <T> Showable<T> addMoveAndShowToQueue(Showable<T> show) {
        moveAndShows.add(show);
        return show;
    }



    public void move() {
        Showable<?> nextShow;
        while ((nextShow = moveAndShows.poll()) != null) {
            nextShow.getAndShow();
            if (nextShow.isBreakChain())
                return;
        }
    }

}
