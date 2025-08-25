package javacplus.Handlers.Command;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import javacplus.Handlers.Component.ComponentHandler;
import javacplus.Handlers.Database.DatabaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ContextCommand {
    private final MessageReceivedEvent event;
    private final DatabaseHandler mainDatabase;
    private final ComponentHandler componentHandler;

    public ContextCommand(MessageReceivedEvent event, ComponentHandler componentHandler, DatabaseHandler database) {
        this.event = event;
        this.mainDatabase = database;
        this.componentHandler = componentHandler;
    }

    public ComponentHandler getComponentHandler() {
        return this.componentHandler;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public String[] getArgs() {
        return event.getMessage().getContentRaw().trim().split("\\s+");
    }

    public Connection getSQLConnection(String name) {
        try {
            return mainDatabase.getConnection(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TextChannel getChannel() {
        return event.getChannel().asTextChannel();
    }

    public void sendMessage(String msg) {
        getChannel().sendMessage(msg).queue();
    }

    public void sendMessageAfter(String msg, int time, TimeUnit timeUnit) {
        getChannel().sendMessage(msg).queueAfter(time, timeUnit);
    }

    public Member getAuthorMember() {
        return event.getMember();
    }

    public String getAuthorId() {
        return event.getAuthor().getId();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public Message getMessage() {
        return event.getMessage();
    }

    public String getServerId() {
        return event.getGuild().getId();
    }

    public String getAuthorName() {
        return event.getAuthor().getName();
    }

    public String getMessageContent() {
        return event.getMessage().getContentRaw();
    }
}
