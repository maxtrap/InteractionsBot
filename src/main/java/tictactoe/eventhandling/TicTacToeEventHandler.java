package tictactoe.eventhandling;

import eventhandling.EventHandler;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
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

}
