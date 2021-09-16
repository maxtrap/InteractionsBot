package tictactoe.view;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.Component;
import tictactoe.guts.*;
import tictactoe.guts.gameactions.Move;
import tictactoe.guts.gamestate.CellEntry;
import tictactoe.guts.gamestate.GameBoard;
import tictactoe.guts.gamestate.GameEnd;
import tictactoe.guts.gamestate.GridPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TicTacToeDisplay {

    public static final String X_EMOJI = "<:X_:886689752311550035>";
    public static final String O_EMOJI = "<:O_:886689771139788800>";

    public static final String EASY_BUTTON_ID = "easy";
    public static final String MEDIUM_BUTTON_ID = "medium";
    public static final String IMPOSSIBLE_BUTTON_ID = "impossible";
    public static final String FORFEIT_BUTTON_ID = "forfeit";

    private TicTacToeDisplay() {
    }

    public static void showPreGame(SlashCommandEvent event, Consumer<? super Long> messageIdConsumer) {
        event.reply(createPreGameMessage(String.format("Welcome %s to Tic-tac-toe! Choose the AI difficulty.", event.getMember().getAsMention())))
                .flatMap(InteractionHook::retrieveOriginal)
                .queue(message -> messageIdConsumer.accept(message.getIdLong()));
    }

    public static void showGameStart(InteractionHook hook, TicTacToeGame game) {
        hook.editOriginal(createTicTacToeMessage(
                game.getGameboard(),
                String.format("Playing tic-tac-toe against %s | Press any button to start.", hook.getInteraction().getUser().getAsMention())
                ))
                .queue();
    }

    public static void showForfeit(InteractionHook hook) {
        hook.editOriginal(String.format("Tic-tac-toe against %1$s | %1$s has forfeited", hook.getInteraction().getUser().getAsMention())).queue();
    }

    public static void showMove(InteractionHook hook, Move move) {
        showMove(hook, move, null);
    }

    public static void showMove(InteractionHook hook, Move move, Runnable thenRun) {
        hook.editOriginal(
                createTicTacToeMessage(
                        move.gameBoard(),
                        String.format(
                                "Playing tic-tac-toe against %s | Current turn: %s",
                                hook.getInteraction().getMember().getAsMention(),
                                getEmojiStringFromEntry(move.nextTurn())
                        )
                )
        ).queue(message -> {
            if (thenRun != null)
                thenRun.run();
        });
    }

    public static void showGameEnd(InteractionHook hook, GameEnd gameEnd) {
        if (gameEnd.isDraw()) {
            hook.editOriginal(createTicTacToeMessage(
                    gameEnd.gameBoard(),
                    String.format("Tic-tac-toe against %s | Draw", hook.getInteraction().getMember().getAsMention()),
                    ButtonStyle.PRIMARY
            )).queue();
            return;
        }

        hook.editOriginal(createTicTacToeMessage(
                gameEnd.gameBoard(),
                String.format("Tic-tac-toe against %s | %s",
                        hook.getInteraction().getMember().getAsMention(),
                        gameEnd.winner() == TicTacToeGame.START_MOVE ? "You win!" : "You lose"),
                gameEnd.winRowIds()::contains,
                ButtonStyle.SUCCESS,
                ButtonStyle.SECONDARY
        )).queue();
    }




    private static Message createPreGameMessage(String mainMessage) {
        MessageBuilder builder = new MessageBuilder(mainMessage);

        builder.setActionRows(ActionRow.of(
                Button.success(EASY_BUTTON_ID, "Easy"),
                Button.secondary(MEDIUM_BUTTON_ID, "Medium"),
                Button.primary(IMPOSSIBLE_BUTTON_ID, "Impossible"),
                Button.danger(FORFEIT_BUTTON_ID, "Cancel")
        ));

        return builder.build();
    }

    private static Message createTicTacToeMessage(GameBoard gameBoard, String mainMessage) {
        return createTicTacToeMessage(gameBoard, mainMessage, ButtonStyle.SECONDARY);
    }

    private static Message createTicTacToeMessage(GameBoard gameBoard, String mainMessage, ButtonStyle style) {
        return createTicTacToeMessage(gameBoard, mainMessage, id -> false, null, style);
    }

    private static Message createTicTacToeMessage(GameBoard gameBoard, String mainMessage, Predicate<? super Integer> condition, ButtonStyle successStyle, ButtonStyle defaultStyle) {
        MessageBuilder builder = new MessageBuilder(mainMessage);

        builder.setActionRows(createActionRows(gameBoard, condition, successStyle, defaultStyle));

        return builder.build();
    }

    private static Collection<ActionRow> createActionRows(GameBoard gameBoard, Predicate<? super Integer> condition, ButtonStyle successStyle, ButtonStyle defaultStyle) {
        Collection<ActionRow> actionsRows = createButtons(gameBoard, condition, successStyle, defaultStyle).stream().map(ActionRow::of).collect(Collectors.toList());
        actionsRows.add(ActionRow.of(Button.danger(FORFEIT_BUTTON_ID, "Forfeit")));
        return actionsRows;
    }

    private static List<List<Component>> createButtons(GameBoard gameBoard, Predicate<? super Integer> condition, ButtonStyle successStyle, ButtonStyle defaultStyle) {
        List<List<Component>> buttons = new ArrayList<>(TicTacToeGame.GRID_SIZE);
        for (int row = 0; row < TicTacToeGame.GRID_SIZE; row++) {
            List<Component> buttonRow = new ArrayList<>(TicTacToeGame.GRID_SIZE);
            for (int col = 0; col < TicTacToeGame.GRID_SIZE; col++) {
                buttonRow.add(getButtonTypeIfElse(gameBoard.getEntry(row, col), GridPosition.getGridPositionId(row, col), condition, successStyle, defaultStyle));
            }
            buttons.add(buttonRow);
        }
        return buttons;
    }


    private static Button getButtonTypeIfElse(CellEntry entry, int id, Predicate<? super Integer> condition, ButtonStyle success, ButtonStyle fail) {
        return switch (entry) {
            case X -> Button.of(condition.test(id) ? success : fail, String.valueOf(id), Emoji.fromMarkdown(X_EMOJI));
            case O -> Button.of(condition.test(id) ? success : fail, String.valueOf(id), Emoji.fromMarkdown(O_EMOJI));
            default -> Button.of(condition.test(id) ? success : fail, String.valueOf(id), "\u200B");
        };
    }

    private static String getEmojiStringFromEntry(CellEntry entry) {
        return switch (entry) {
            case X -> X_EMOJI;
            case O -> O_EMOJI;
            default -> null;
        };
    }

}
