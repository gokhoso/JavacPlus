package net.JavacClassic.Handlers.Command;

import java.util.concurrent.TimeUnit;

import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Handlers.Component.ComponentMain;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ContextCommand {

    private final MessageReceivedEvent event;
    private final ComponentMain componentMain;
    private final CUser cUser;

    public ContextCommand(
            MessageReceivedEvent event,
            ComponentMain componentMain,
            CUser cUser
    ) {
        this.event = event;
        this.componentMain = componentMain;
        this.cUser = cUser;
    }

    public CUser getcUser() { return cUser; }

    public SelfUser getSelfUser() {
        return this.event.getJDA().getSelfUser();
    }

    public ComponentMain getComponentHandler() {
        return this.componentMain;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public String[] getArgs() {
        return event.getMessage().getContentRaw().trim().split("\\s+");
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
