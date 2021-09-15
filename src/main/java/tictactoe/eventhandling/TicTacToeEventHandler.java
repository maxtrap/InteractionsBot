package tictactoe.eventhandling;

import eventhandling.EventHandler;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import tictactoe.guts.GameBoard;
import tictactoe.guts.MoveFactory;
import tictactoe.guts.TicTacToeGame;
import tictactoe.view.TicTacToeDisplay;

import java.util.HashMap;
import java.util.Map;

public class TicTacToeEventHandler implements EventHandler {

    private record GameAndId(TicTacToeGame game, long messageId) {
    }

    private final Map<Long, GameAndId> userIdToGameAndId;

    public TicTacToeEventHandler() {
        userIdToGameAndId = new HashMap<>();
    }

    @Override
    public void handleSlash(SlashCommandEvent event) {
        if (event.getName().equals("tictactoe")) {
            if (userIdToGameAndId.containsKey(event.getUser().getIdLong())) {
                event.reply("You are already playing a game").setEphemeral(true).queue();
                return;
            }

            new MoveFactory()
                    .startNewGame()
                    .thenShow(game -> TicTacToeDisplay.showGameStart(event, game, messageId -> userIdToGameAndId.put(event.getUser().getIdLong(), new GameAndId(game, messageId))))
                    .move();
        }
    }

    @Override
    public void handleButton(ButtonClickEvent event) {
        if (!userIdToGameAndId.containsKey(event.getUser().getIdLong())) {
            event.reply("Someone else is playing this game or it has already ended. Type /tictactoe to start your own game.").setEphemeral(true).queue();
            return;
        }

        if (event.getMessageIdLong() != userIdToGameAndId.get(event.getUser().getIdLong()).messageId()) {
            event.reply("Someone else is playing this game or it has already ended.").setEphemeral(true).queue();
            return;
        }

        event.deferEdit().queue();
        InteractionHook hook = event.getHook();

        TicTacToeGame game = userIdToGameAndId.get(event.getUser().getIdLong()).game();
        GameBoard gameBoard = game.getGameboard();

        new MoveFactory(game)
                .playerMove(Integer.parseInt(event.getComponentId()))
                .thenShowThenAsync((move, next) -> TicTacToeDisplay.showMove(hook, gameBoard, move, next))
                .checkGameEnd()
                .thenShowThenAsync((gameEnd, next) -> TicTacToeDisplay.showGameEnd(hook, gameBoard, gameEnd, next))
                .computerMove()
                .thenShow(move -> TicTacToeDisplay.showMove(hook, gameBoard, move))
                .checkGameEnd()
                .thenShowThenAsync((gameEnd, next) -> TicTacToeDisplay.showGameEnd(hook, gameBoard, gameEnd, next))
                .move();
    }
}
