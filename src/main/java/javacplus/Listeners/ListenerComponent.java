package javacplus.Listeners;

import javax.annotation.Nonnull;

import javacplus.Entities.UserButton;
import javacplus.Handlers.Component.ComponentContext;
import javacplus.Handlers.Component.ComponentMain;
import javacplus.Handlers.Database.DatabaseHandler;
import javacplus.Interfaces.IButton;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerComponent extends ListenerAdapter {
    private final DatabaseHandler database;
    private final ComponentMain componentMain;

    public ListenerComponent(ComponentMain componentMain,  DatabaseHandler database) {
        this.database = database;
        this.componentMain = componentMain;
    }

    @Override
    public void onGenericComponentInteractionCreate(@Nonnull GenericComponentInteractionCreateEvent event) {
        String messageId = event.getMessageId();
        String userId =  event.getUser().getId();

        UserButton button = componentMain.getButtonManager().getRegistry().getUserRegistry(userId);
        String buttonComponentId = button.getInformation()[0];
        String buttonMessageId = button.getInformation()[1];

        System.err.println("ButtonComponent Id: " + buttonComponentId);

        if (messageId.equals(buttonMessageId)) {
            ComponentContext ctx = new ComponentContext(event, database);
            IButton button2 = componentMain.getButtonManager().getRegistry().getButton(buttonComponentId); 
            button2.execute(ctx);
        }
    }
}
