package tictactoe.guts.gameactions;

import java.util.function.Supplier;

abstract class Showable<T> {
    private final MoveFactory moveFactory;
    private final Supplier<? extends T> toShow;
    private boolean breakChain;

    protected Showable(MoveFactory moveFactory, Supplier<? extends T> toShow) {
        this.moveFactory = moveFactory;
        this.toShow = toShow;
    }

    protected MoveFactory moveFactory() {
        return moveFactory;
    }

    protected T getToShow() {
        return toShow.get();
    }

    protected void setBreakChain() {
        this.breakChain = true;
    }

    abstract void getAndShow();

    boolean isBreakChain() {
        return breakChain;
    }
}
