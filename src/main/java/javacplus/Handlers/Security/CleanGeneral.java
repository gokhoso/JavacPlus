package javacplus.Handlers.Security;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CleanGeneral {
    private final String botChannelID = "1395433061541216256";
    private final String generalChannelID = "1395427284231651421";
    private final int MAX_STRING_LENGTH = 350;

    private String getLink(String guildID, String channelID, String messageID) {
        return String.format("https://discord.com/channels/%s/%s/%s", guildID, channelID, messageID);
    }

    private void cleanLongBotMessages(MessageReceivedEvent event) {
        System.err.println("Test");

        final boolean isBot = event.getAuthor().isBot();
        final boolean isClient = event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId());
        final boolean isLarge = event.getMessage().getContentRaw().length() > MAX_STRING_LENGTH;
        final boolean isMessageFromGeneral = event.getChannel().getId().equals(generalChannelID);
        final String text = "ah, " + event.getMember().getAsMention()
                + " oldukça sinir bozucusun. Mesajını bot kanalına yönlendiriyorum...";

        if (isMessageFromGeneral && !isClient && isBot && isLarge) {
            event.getChannel().asTextChannel().sendMessage(text).queue(msg2 -> {
                event.getMessage().delete().queue();

                TextChannel botChannel = event.getGuild().getTextChannelById(botChannelID);
                if (botChannel != null) {
                    botChannel.sendMessage(event.getMessage().getContentRaw()).queue(
                            msgBot -> {
                                String link = getLink(event.getGuild().getId(), botChannelID, msgBot.getId());
                                msg2.editMessage(text + "\nBot kanalından link: " + link).queue();
                                msg2.delete().queueAfter(10, TimeUnit.SECONDS);
                            });
                }
            });
        }
    }

    public void execute(MessageReceivedEvent event) {
        cleanLongBotMessages(event);
    }
}
