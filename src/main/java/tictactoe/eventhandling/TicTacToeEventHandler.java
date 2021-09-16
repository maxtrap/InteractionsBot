package tictactoe.eventhandling;

import eventhandling.EventHandler;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import tictactoe.guts.gamestate.GameEnd;
import tictactoe.guts.gameactions.MoveFactory;
import tictactoe.guts.TicTacToeGame;
import tictactoe.view.TicTacToeDisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
        long userId = event.getUser().getIdLong();
        if (!userIdToGameAndId.containsKey(userId)) {
            event.reply("This game has already ended or someone else is playing it. Type /tictactoe to start your own game.").setEphemeral(true).queue();
            return;
        }

        if (event.getMessageIdLong() != userIdToGameAndId.get(userId).messageId()) {
            event.reply("This game has already ended or someone else is playing it.").setEphemeral(true).queue();
            return;
        }

        event.deferEdit().queue();
        InteractionHook hook = event.getHook();

        TicTacToeGame game = userIdToGameAndId.get(userId).game();

        int playerMove = Integer.parseInt(event.getComponentId());
        Consumer<GameEnd> showGameEnd = gameEnd -> {
            TicTacToeDisplay.showGameEnd(hook, gameEnd);
            userIdToGameAndId.remove(userId);
        };

        new MoveFactory(game)
                .checkIfValidMove(playerMove)
                .thenShowBreakChain(unused -> {})
                .playerMove(playerMove)
                .thenShowThenAsync((move, next) -> TicTacToeDisplay.showMove(hook, move, next))
                .checkGameEnd()
                .thenShowBreakChain(showGameEnd)
                .computerMove()
                .thenShow(move -> TicTacToeDisplay.showMove(hook, move))
                .checkGameEnd()
                .thenShowBreakChain(showGameEnd)
                .move();
    }
}
