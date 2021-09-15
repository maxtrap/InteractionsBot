package eventhandling;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface EventHandler {

    default void handleSlash(SlashCommandEvent event) {
        event.reply("This process isn't available right now :frowning:").setEphemeral(true).queue();
    }

    default void handleButton(ButtonClickEvent event) {
        event.reply("This process isn't available right now :frowning:").setEphemeral(true).queue();
    }

}
