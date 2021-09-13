package tictactoe.view;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import tictactoe.guts.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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




    private Message createTicTacToeMessage(String mainMessage) {
        MessageBuilder builder = new MessageBuilder(mainMessage);

        builder.setActionRows(createActionRows());

        return builder.build();
    }

    private Collection<ActionRow> createActionRows() {
        List<ActionRow> actionRows = new ArrayList<>(TicTacToeGame.GRID_SIZE);

        for (int row = 0; row < TicTacToeGame.GRID_SIZE; row++) {
            actionRows.add(createActionRow(row));
        }

        return actionRows;
    }

    private ActionRow createActionRow(int row) {
        List<Component> buttons = new ArrayList<>(TicTacToeGame.GRID_SIZE);

        for (int col = 0; col < TicTacToeGame.GRID_SIZE; col++) {
            buttons.add(getButtonWithEntry(gameBoard.getEntry(row, col), row, col));
        }

        return ActionRow.of(buttons);
    }

    private static Button getButtonWithEntry(CellEntry entry, int row, int col) {
        return switch (entry) {
            case X -> Button.secondary(GridPosition.getGridPositionId(row, col), Emoji.fromMarkdown(X_EMOJI));
            case O -> Button.secondary(GridPosition.getGridPositionId(row, col), Emoji.fromMarkdown(O_EMOJI));
            default -> Button.secondary(GridPosition.getGridPositionId(row, col), "\u200B");
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
