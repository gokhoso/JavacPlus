package javacplus.Handlers.Component;

import javacplus.Handlers.Database.DatabaseHandler;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public class ComponentContext {
    private final GenericComponentInteractionCreateEvent event;
    private final DatabaseHandler mainDatabase;

    public ComponentContext(GenericComponentInteractionCreateEvent event, DatabaseHandler database) {
        this.event = event;
        this.mainDatabase = database;
    }

    public GenericComponentInteractionCreateEvent getEvent() {
        return this.event;
    }

    public DatabaseHandler getDatabase() {
        return this.mainDatabase;
    }
}
