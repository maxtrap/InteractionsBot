package tictactoe.eventhandling;

import eventhandling.EventHandler;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import tictactoe.guts.MoveFactory;
import tictactoe.guts.TicTacToeGame;
import tictactoe.view.TicTacToeDisplay;

public class TicTacToeEventHandler extends EventHandler {

    private final TicTacToeGame game;
    private TicTacToeDisplay display;

    public TicTacToeEventHandler() {
        game = new TicTacToeGame();
    }

    @Override
    public void handleSlash(SlashCommandEvent event) {
        if (event.getName().equals("tictactoe")) {
            game.startGame(gameBoard -> display = new TicTacToeDisplay(event, gameBoard));
        }
    }

    @Override
    public void handleButton(ButtonClickEvent event) {
        event.deferEdit().queue();
        InteractionHook hook = event.getHook();

        new MoveFactory(game)
                .playerMove(Integer.parseInt(event.getComponentId()))
                .thenShowThenAsync((move, next) -> display.showMove(hook, move, next))
                .checkGameEnd()
                .thenShowThenAsync((gameEnd, next) -> display.showGameEnd(hook, gameEnd, next))
                .computerMove()
                .thenShow(move -> display.showMove(hook, move))
                .checkGameEnd()
                .thenShowThenAsync((gameEnd, next) -> display.showGameEnd(hook, gameEnd, next))
                .move();
    }
}
