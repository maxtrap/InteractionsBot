package eventhandling;

import controller.BotController;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventListener extends ListenerAdapter {

    private final List<EventHandler> eventHandlers;

    public EventListener(BotController controller) {
        eventHandlers = controller.getGameHandlers();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        eventHandlers.forEach(handler -> handler.handleSlash(event));
    }
}
