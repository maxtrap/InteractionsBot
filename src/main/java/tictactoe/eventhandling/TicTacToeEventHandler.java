package tictactoe.eventhandling;

import eventhandling.EventHandler;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import tictactoe.guts.TicTacToeGame;
import tictactoe.guts.gameactions.MoveFactory;
import tictactoe.guts.gamestate.GameEnd;
import tictactoe.guts.movealgorithms.MoveAlgorithmOption;
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

            TicTacToeDisplay.showPreGame(event, messageId -> userIdToGameAndId.put(event.getUser().getIdLong(), new GameAndId(new TicTacToeGame(), messageId)));

//            new MoveFactory()
//                    .startNewGame()
//                    .thenShow(game -> TicTacToeDisplay.showGameStart(event, game, messageId -> userIdToGameAndId.put(event.getUser().getIdLong(), new GameAndId(game, messageId))))
//                    .move();
        }
    }

    @Override
    public void handleButton(ButtonClickEvent event) {
        if (!checkIfUserIsValid(event)) return; //Return early if user is not associated with this game (to prevent sabotage)

        event.deferEdit().queue();
        if (handlePreGameInput(event)) return; //Return early if pregame input was successfully handled
        handleGameInput(event);
    }

    private boolean checkIfUserIsValid(ButtonClickEvent event) {
        if (!userIdToGameAndId.containsKey(event.getUser().getIdLong())) {
            event.reply("This game has already ended or someone else is playing it. Type /tictactoe to start your own game.").setEphemeral(true).queue();
            return false;
        }
        if (event.getMessageIdLong() != userIdToGameAndId.get(event.getUser().getIdLong()).messageId()) {
            event.reply("This game has already ended or someone else is playing it.").setEphemeral(true).queue();
            return false;
        }
        return true;
    }

    private boolean handlePreGameInput(ButtonClickEvent event) {
        MoveAlgorithmOption algorithm = switch (event.getComponentId()) {
            case TicTacToeDisplay.EASY_BUTTON_ID -> MoveAlgorithmOption.EASY;
            case TicTacToeDisplay.MEDIUM_BUTTON_ID -> MoveAlgorithmOption.MEDIUM;
            case TicTacToeDisplay.IMPOSSIBLE_BUTTON_ID -> MoveAlgorithmOption.IMPOSSIBLE;
            default -> null;
        };

        if (algorithm != null) {
            new MoveFactory(userIdToGameAndId.get(event.getUser().getIdLong()).game())
                    .startNewGame(algorithm)
                    .thenShow(game -> TicTacToeDisplay.showGameStart(event.getHook(), game))
                    .move();
            return true;
        }
        return false;
    }

    private void handleGameInput(ButtonClickEvent event) {
        long userId = event.getUser().getIdLong();
        InteractionHook hook = event.getHook();
        TicTacToeGame game = userIdToGameAndId.get(userId).game();

        if (event.getComponentId().equals(TicTacToeDisplay.FORFEIT_BUTTON_ID)) {
            TicTacToeDisplay.showForfeit(hook);
            userIdToGameAndId.remove(userId);
            return;
        }

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
