package eventhandling;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public abstract class EventHandler {

    public void handleSlash(SlashCommandEvent event) {
        event.reply("This process isn't available right now").setEphemeral(true).queue();
    }

    public void handleButton() {

    }

}
