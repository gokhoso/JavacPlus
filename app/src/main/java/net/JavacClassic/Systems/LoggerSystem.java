package net.JavacClassic.Systems;

import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Config;
import net.JavacClassic.Entites.CachedMessage;
import net.JavacClassic.Entites.CachedUser;
import net.JavacClassic.Entites.UserMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

public class LoggerSystem {

  private final CUser cUser;
  private final Logger logger = LoggerFactory.getLogger(LoggerSystem.class);

  public LoggerSystem(CUser cUser) {
    this.cUser = cUser;
  }

  private EmbedBuilder LoggerEmbedBuilder(
    CachedUser user,
    UserMessage message,
    String desc
  ) {
    String channelMention = String.format("<#%s>", message.getChannelId());
    String userMention = String.format("<@%s>", user.getUserId());

    return new EmbedBuilder()
      .setAuthor(user.getUsername(), null, user.getAvatarUrl())
      .setDescription(desc)
      .addField("Profil", userMention, true)
      .addField("Kanal", channelMention, true)
      .setFooter(user.getGuildName())
      .setTimestamp(Instant.now());
  }

  private List<UserMessage> getUserMessages(
    CachedMessage cMessage,
    String eventMessageId
  ) {
    if (cMessage == null) {
      logger.debug("CachedMessage is NULL!");
      return null;
    }

    if (cMessage.getId().equals(eventMessageId)) {
      CachedUser cachedUser = cUser.getCache(cMessage.getAuthorId());

      if (cachedUser == null) {
        logger.debug("CachedUser is NULL!");
        return null;
      }

      UserMessage msg = cachedUser.getUserMessages().getLast();

      if (msg == null) {
        logger.debug("UserMessage is NULL!");
        return null;
      }

      if (msg.getContent().isEmpty()) {
        logger.debug("Message is EMPTY!");
        return null;
      }

      return cachedUser.getUserMessages();
    }

    return null;
  }

  public void logDeletedMessage(MessageDeleteEvent event) {
    String messageId = event.getMessageId();
    CachedMessage cachedMessage = cUser.getCacheMessage(messageId);
    CachedUser cachedUser = cUser.getCache(cachedMessage.getAuthorId());

    if (cachedMessage == null || cachedUser == null) {
      logger.debug("CachedMessage or CachedUser is NULL!");
      return;
    }

    UserMessage msg = getUserMessages(
      cachedMessage,
      event.getMessageId()
    ).getLast();

    if (msg == null) {
      logger.debug("Message is NULL!");
      return;
    }

    EmbedBuilder embed = LoggerEmbedBuilder(
      cachedUser,
      msg,
      ":wastebasket: **Mesaj silindi**"
    );

    embed.addField("Mesaj", msg.getContent(), false);

    TextChannel logChannel = event
      .getGuild()
      .getTextChannelById(Config.data.channels.log);

    if (logChannel == null) {
      logger.debug("LogChanel is NULL!");
      return;
    }

    logChannel.sendMessageEmbeds(embed.build()).queue();
  }

  public void logEditedMessage(MessageUpdateEvent event) {
    String messageId = event.getMessageId();
    CachedMessage cachedMessage = cUser.getCacheMessage(messageId);

    if (cachedMessage.getId().equals(event.getMessageId())) {
      CachedUser user = cUser.getCache(cachedMessage.getAuthorId());
      if (user == null) {
        logger.debug("User is NULL!");
        return;
      }

      Message updatedMessage = event.getMessage();
      UserMessage[] prevMessage = new UserMessage[1];

      List<UserMessage> messages = getUserMessages(cachedMessage, messageId);

      if (messages == null || messages.isEmpty()) return;
      messages.forEach(msg -> {
        if (msg.getMessageId().equals(event.getMessageId())) {
          prevMessage[0] = msg;
        }
      });

      if (prevMessage[0] == null) {
        return;
      }

      if (
        updatedMessage.getContentRaw().isEmpty() ||
        prevMessage[0].getContent().isEmpty() ||
        updatedMessage.getContentRaw().equals(prevMessage[0].getContent())
      ) {
        return;
      }

      EmbedBuilder embed = LoggerEmbedBuilder(
        user,
        prevMessage[0],
        "🗘 **Mesaj Güncellendi**"
      );

      embed.addField("Yeni Hali", updatedMessage.getContentRaw(), false);
      embed.addField("Eski Hali", prevMessage[0].getContent(), true);

      TextChannel logChannel = event
        .getGuild()
        .getTextChannelById(Config.data.channels.log);

      if (logChannel == null) {
        return;
      }

      logChannel.sendMessageEmbeds(embed.build()).queue();
    }
  }
}
