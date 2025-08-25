package javacplus.Listeners;

import java.util.Optional;

import javax.annotation.Nonnull;

import javacplus.Handlers.Component.ComponentContext;
import javacplus.Handlers.Component.ComponentHandler;
import javacplus.Handlers.Database.DatabaseHandler;
import javacplus.Interfaces.IComponent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerComponent extends ListenerAdapter {
    private final ComponentHandler componentHandler;
    private final DatabaseHandler database;

    public ListenerComponent(ComponentHandler componentHandler, DatabaseHandler database) {
        this.componentHandler = componentHandler;
        this.database = database;
    }

    @Override
    public void onGenericComponentInteractionCreate(@Nonnull GenericComponentInteractionCreateEvent event) {
        Optional<String> componentUserIdOptional = componentHandler.getUserComponent(event.getUser().getId());
        Optional<IComponent> componentIdOptional = componentHandler.getComponent(event.getComponentId());
        if (!componentUserIdOptional.isPresent() || !componentIdOptional.isPresent()) {
            System.err.println("No component ID or UserComponentid found! " + event.getUser().getName());
            return;
        }

        String componentUserId = componentUserIdOptional.get();
        IComponent component = componentIdOptional.get();
        if (event.getComponentId().equals(componentUserId)) {
            System.err.println("Component matched! " + event.getUser().getName());
            ComponentContext ctx = new ComponentContext(event, database);
            component.execute(ctx);
            return;
        }
    }
}
