package javacplus.Listeners;

import javax.annotation.Nonnull;

import javacplus.Handlers.Command.CommandHandler;
import javacplus.Handlers.Component.ComponentHandler;
import javacplus.Handlers.Database.DatabaseHandler;
import javacplus.Handlers.Security.CleanGeneral;
import javacplus.Handlers.Security.SpamProtect;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerMessage extends ListenerAdapter {
    private final SpamProtect spamProtect;
    private final DatabaseHandler mainDatabase;
    private final CommandHandler commandHandler;
    private final ComponentHandler componentHandler;

    public ListenerMessage(CommandHandler commandHandler, ComponentHandler componentHandler,
            DatabaseHandler mainDatabase, SpamProtect spamProtect) {
        this.commandHandler = commandHandler;
        this.spamProtect = spamProtect;
        this.mainDatabase = mainDatabase;
        this.componentHandler = componentHandler;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        System.out.println("Message: " + event.getMessage().getContentRaw());
        spamProtect.handleMessage(event);

        if (spamProtect.shouldLimit(event))
            return;

        new CleanGeneral().execute(event);
        commandHandler.execute(event, componentHandler, mainDatabase);
    }
}
