package tictactoe.guts.gameactions;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ShowBreakChain<T> extends Showable<T> {
    private final Predicate<? super T> breakChainTest;
    private Consumer<? super T> thenShow;

    ShowBreakChain(MoveFactory moveFactory, Supplier<? extends T> toShow, Predicate<? super T> breakChainTest) {
        super(moveFactory, toShow);
        this.breakChainTest = breakChainTest;
    }

    public MoveFactory thenShowBreakChain(Consumer<? super T> thenShow) {
        this.thenShow = thenShow;
        return moveFactory();
    }

    @Override
    void getAndShow() {
        T toShow = getToShow();
        if (breakChainTest.test(toShow)) {
            thenShow.accept(toShow);
            setBreakChain();
        }
    }
}
