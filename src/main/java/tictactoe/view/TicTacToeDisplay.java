package tictactoe.view;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import tictactoe.guts.CellEntry;
import tictactoe.guts.GameBoard;
import tictactoe.guts.Move;
import tictactoe.guts.TicTacToeGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TicTacToeDisplay {

    public static final String X_EMOJI = "<:X_:886689752311550035>";
    public static final String O_EMOJI = "<:O_:886689771139788800>";


    private final GameBoard gameBoard;

    public TicTacToeDisplay(SlashCommandEvent event, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        event.reply(createTicTacToeMessage()).queue();
    }


    public void showMove(ButtonClickEvent event, Move move) {
        event.editMessage("edited!").queue();
        event.editButton(getButtonWithEntry(move.move(), move.row(), move.col())).queue();
    }





    private Message createTicTacToeMessage() {
        MessageBuilder builder = new MessageBuilder("Tic-tac-toe! You vs the computer");

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
            case X -> Button.secondary(TicTacToeGame.getGridPositionId(row, col), Emoji.fromMarkdown(X_EMOJI));
            case O -> Button.secondary(TicTacToeGame.getGridPositionId(row, col), Emoji.fromMarkdown(O_EMOJI));
            default -> Button.secondary(TicTacToeGame.getGridPositionId(row, col), "\u200B");
        };
    }

}