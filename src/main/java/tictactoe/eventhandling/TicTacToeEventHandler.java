package tictactoe.eventhandling;

import eventhandling.EventHandler;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import tictactoe.guts.GridPosition;
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
        game.playerMove(
                GridPosition.getGridPositionFromId(event.getComponentId()), //Send player move to game object
                move -> display.showMove(hook, move, //Display the player's move
                        () -> game.computerMove( //Tell computer to generate its move
                                computerMove -> display.showMove(hook, computerMove) //Display the computer's move
                        )
                )
        );
    }
}
