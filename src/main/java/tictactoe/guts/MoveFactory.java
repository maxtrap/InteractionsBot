package tictactoe.guts;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MoveFactory {

    private final TicTacToeGame game;
    private final Queue<Show<?>> moveAndShows;

    public MoveFactory(TicTacToeGame game) {
        this.game = game;
        moveAndShows = new ArrayDeque<>();
    }


    //thenShowThenAsync() documentation draft:
    //Same as thenShow, except that it will  allow you to call the next method asynchronously
    //The show part is still called synchronously in relation to the previous move
    //*** THIS METHOD MUST BE CALLED WHEN THE NEXT METHOD IS ASYNC, EVEN IF THIS ONE ISN'T ***
    //This method acts as a turning point for this class. Before it, all methods well be synced with the initial move() call
    //All methods after will be synced with relation to when this runnable is called

    public class Show<T> {
        private final Supplier<? extends T> toShow;
        private Consumer<? super T> thenShow;
        private BiConsumer<? super T, Runnable> thenShowThenAsync;

        private Show(Supplier<? extends T> toShow) {
            this.toShow = toShow;
        }

        public MoveFactory thenShow(Consumer<? super T> thenShow) {
            this.thenShow = thenShow;
            return MoveFactory.this;
        }

        public MoveFactory thenShowThenAsync(BiConsumer<? super T, Runnable> thenShowThenAsync) {
            this.thenShowThenAsync = thenShowThenAsync;
            return MoveFactory.this;
        }

        private void moveAndShow() {
            thenShow.accept(toShow.get());
        }

        private void moveAndShowAsync() {
            thenShowThenAsync.accept(toShow.get(), MoveFactory.this::move);
        }
    }

    public Show<Move> playerMove(int gridPositionId) {
        return addMoveAndShowToQueue(new Show<>(() -> game.playerMove(gridPositionId)));
    }

    public Show<Move> computerMove() {
        return addMoveAndShowToQueue(new Show<>(game::computerMove));
    }

    public Show<GameEnd> checkGameEnd() {
        return addMoveAndShowToQueue(new Show<>(game::getGameEnd));
    }



    private <T> Show<T> addMoveAndShowToQueue(Show<T> show) {
        moveAndShows.add(show);
        return show;
    }



    public void move() {
        Show<?> nextShow;
        while ((nextShow = moveAndShows.poll()) != null && nextShow.thenShow != null) {
            nextShow.moveAndShow();
        }
        if (nextShow != null) {
            nextShow.moveAndShowAsync();
        }
    }

}
