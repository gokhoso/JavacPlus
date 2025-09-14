package net.JavacClassic.Listeners;

import javax.annotation.Nonnull;

import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Handlers.Command.CommandMain;
import net.JavacClassic.Handlers.Component.ComponentMain;
import net.JavacClassic.Systems.BumpReminder;
import net.JavacClassic.Systems.LoggerSystem;
import net.JavacClassic.Systems.SpamProtect;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LMessage extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(LMessage.class);

    private final ComponentMain componentMain;
    private final CommandMain commandMain;
    private final LoggerSystem logSystem;
    private final CUser cUser;

    SpamProtect sProtect = new SpamProtect();
    BumpReminder bReminder = new BumpReminder();

    public LMessage(
            CUser cUser,
            CommandMain commandMain,
            ComponentMain componentMain
    ) {
        this.cUser = cUser;
        this.componentMain = componentMain;
        this.commandMain = commandMain;
        this.logSystem = new LoggerSystem(cUser);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        cUser.addCache(event);
        commandMain.execute(event, componentMain, cUser);

        sProtect.handleMessage(event);
        bReminder.run(event);
    }

    @Override
    public void onMessageDelete(@Nonnull MessageDeleteEvent event) {
        logger.info("Triggered delete");
        logSystem.logDeletedMessage(event);
    }

    @Override
    public void onMessageUpdate(@Nonnull MessageUpdateEvent event) {
        logSystem.logEditedMessage(event);
    }
}
