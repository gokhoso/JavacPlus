package net.JavacClassic.Utils;

import java.time.Instant;
import net.JavacClassic.Entites.CachedMessage;
import net.JavacClassic.Entites.CachedUser;
import net.JavacClassic.Entites.UserMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CacheUtils {

  public CachedMessage createMessageCache(MessageReceivedEvent event) {
    return new CachedMessage(
      event.getMessageId(),
      event.getAuthor().getId(),
      Instant.now()
    );
  }

  public CachedUser createUserCache(
    MessageReceivedEvent event,
    UserMessage msg
  ) {
    String authorId = event.getAuthor().getId();
    String username = event.getAuthor().getName();
    String avatarurl = event.getAuthor().getAvatarUrl();
    Guild guild = event.getGuild();

    return new CachedUser(authorId, username, avatarurl, guild.getName(), msg);
  }

  public UserMessage createMessage(MessageReceivedEvent event) {
    String content = event.getMessage().getContentRaw();
    String authorId = event.getAuthor().getId();
    String channelId = event.getMessage().getChannelId();

    return new UserMessage(content, authorId, channelId, event.getMessageId());
  }
}
