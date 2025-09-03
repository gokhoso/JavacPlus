package javacplus.Listeners;

import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Entities.UserButton;
import javacplus.Handlers.Component.ComponentContext;
import javacplus.Handlers.Component.ComponentMain;
import javacplus.Handlers.Database.DatabaseHandler;
import javacplus.Interfaces.IButton;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerComponent extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(ListenerComponent.class);
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

        List<UserButton> buttons = componentMain.getButtonManager().getRegistry().getUserRegistry(userId);

        if (buttons == null) {
            logger.debug("Buttons is NULL!");
            return;
        }
        
        buttons.forEach(button -> {
            if (button.getInformation().getUniqueMessageId().equals(messageId)) {
                ComponentContext ctx = new ComponentContext(event, database);
                IButton button2 = componentMain.getButtonManager().getRegistry().getButton(button.getInformation().getId()); 
                if (button2 != null) {
                    event.editMessage(event.getMessage().getContentRaw() + "\nButton is being processed <a:tts_loading:1407326874790526997>").queue();
                    button2.execute(ctx);
                    return;
                }
                logger.debug("Button not found!");
            }
        });
    }
}
