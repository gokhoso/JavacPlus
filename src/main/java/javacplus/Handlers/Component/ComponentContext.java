package javacplus.Handlers.Component;

import javacplus.Handlers.Database.DatabaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public class ComponentContext {
    private final GenericComponentInteractionCreateEvent event;
    private final DatabaseHandler database;
    
    public ComponentContext(GenericComponentInteractionCreateEvent event, DatabaseHandler database) {
        this.event = event; this.database = database;
    }

    public GenericComponentInteractionCreateEvent getEvent() {
        return event;
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public DatabaseHandler getDatabase() {
        return database;
    }
}
