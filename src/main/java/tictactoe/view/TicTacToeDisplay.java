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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TicTacToeDisplay {

    public static final String X_EMOJI = "<:X_:886689752311550035>";
    public static final String O_EMOJI = "<:O_:886689771139788800>";


    private final GameBoard gameBoard;

    public TicTacToeDisplay(SlashCommandEvent event, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        event.reply(createTicTacToeMessage( String.format("Welcome %s to Tic-tac-toe! Press any button to start.", event.getMember().getAsMention()) )).queue();
    }


    public void showMove(InteractionHook hook, Move move) {
        showMove(hook, move, null);
    }

    public void showMove(InteractionHook hook, Move move, Runnable thenRun) {
        hook.editOriginal(
                createTicTacToeMessage(
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

    public void showGameEnd(InteractionHook hook, GameEnd gameEnd, Runnable thenRun) {
        if (gameEnd == null) {
            thenRun.run();
            return;
        }

        if (gameEnd.isDraw()) {
            hook.editOriginal(createTicTacToeMessage(
                    String.format("Tic-tac-toe against %s | Draw", hook.getInteraction().getMember().getAsMention()),
                    ButtonStyle.PRIMARY
            )).queue();
            return;
        }

        hook.editOriginal(createTicTacToeMessage(
                String.format("Tic-tac-toe against %s | %s",
                        hook.getInteraction().getMember().getAsMention(),
                        gameEnd.getWinner() == TicTacToeGame.START_MOVE ? "You win!" : "You lose"),
                id -> gameEnd.getWinRowIds().contains(id),
                ButtonStyle.SUCCESS,
                ButtonStyle.SECONDARY
        )).queue();
    }






    private Message createTicTacToeMessage(String mainMessage) {
        return createTicTacToeMessage(mainMessage, ButtonStyle.SECONDARY);
    }

    private Message createTicTacToeMessage(String mainMessage, ButtonStyle style) {
        return createTicTacToeMessage(mainMessage, id -> false, null, style);
    }

    private Message createTicTacToeMessage(String mainMessage, Predicate<? super Integer> condition, ButtonStyle successStyle, ButtonStyle defaultStyle) {
        MessageBuilder builder = new MessageBuilder(mainMessage);

        builder.setActionRows(createActionRows(condition, successStyle, defaultStyle));

        return builder.build();
    }

    private Collection<ActionRow> createActionRows(Predicate<? super Integer> condition, ButtonStyle successStyle, ButtonStyle defaultStyle) {
        return createButtons(condition, successStyle, defaultStyle).stream().map(ActionRow::of).collect(Collectors.toList());
    }

    private List<List<Component>> createButtons(Predicate<? super Integer> condition, ButtonStyle successStyle, ButtonStyle defaultStyle) {
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
