package tictactoe.guts;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Show<T> extends Showable<T> {
    private Consumer<? super T> thenShow;
    private BiConsumer<? super T, Runnable> thenShowThenAsync;

    Show(MoveFactory moveFactory, Supplier<? extends T> toShow) {
        super(moveFactory, toShow);
    }


    public MoveFactory thenShow(Consumer<? super T> thenShow) {
        this.thenShow = thenShow;
        return moveFactory();
    }

    public MoveFactory thenShowThenAsync(BiConsumer<? super T, Runnable> thenShowThenAsync) {
        this.thenShowThenAsync = thenShowThenAsync;
        setBreakChain();
        return moveFactory();
    }

    @Override
    void getAndShow() {
        if (thenShow != null)
            thenShow.accept(getToShow());
        else if (thenShowThenAsync != null)
            thenShowThenAsync.accept(getToShow(), moveFactory()::move);
        else
            throw new IllegalStateException("There is nothing to show");
    }
}
