package tictactoe.guts;

import java.util.ArrayDeque;
import java.util.Queue;

public class MoveFactory {

    private TicTacToeGame game;
    private final Queue<Show<?>> moveAndShows;


    public MoveFactory() {
        this(null);
    }

    public MoveFactory(TicTacToeGame game) {
        this.game = game;
        moveAndShows = new ArrayDeque<>();
    }

    public Show<TicTacToeGame> startNewGame() {
        return addMoveAndShowToQueue(new Show<>(this, () -> game = new TicTacToeGame()));
    }

    public Show<Move> playerMove(int gridPositionId) {
        return addMoveAndShowToQueue(new Show<>(this, () -> game.playerMove(gridPositionId)));
    }

    public Show<Move> computerMove() {
        return addMoveAndShowToQueue(new Show<>(this, game::computerMove));
    }

    public Show<GameEnd> checkGameEnd() {
        return addMoveAndShowToQueue(new Show<>(this, game::getGameEnd));
    }



    private <T> Show<T> addMoveAndShowToQueue(Show<T> show) {
        moveAndShows.add(show);
        return show;
    }



    public void move() {
        Show<?> nextShow;
        while ((nextShow = moveAndShows.poll()) != null) {
            nextShow.getToShow();
            if (nextShow.isBreakChain())
                return;
        }
    }

}
