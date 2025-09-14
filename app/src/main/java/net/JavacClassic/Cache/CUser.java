package net.JavacClassic.Cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.JavacClassic.Entites.CachedMessage;
import net.JavacClassic.Entites.CachedUser;
import net.JavacClassic.Entites.UserMessage;
import net.JavacClassic.Utils.CacheUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CUser {

  private final Map<String, CachedMessage> cMessage = new ConcurrentHashMap<>();
  private final Map<String, CachedUser> cMembers = new ConcurrentHashMap<>();
  private final CacheUtils utils = new CacheUtils();

  public CachedUser getCache(String userId) {
    return cMembers.getOrDefault(userId, null);
  }

  public CachedMessage getCacheMessage(String messageId) {
    return cMessage.getOrDefault(messageId, null);
  }

  public void addCache(MessageReceivedEvent e) {
    CachedMessage cachedMessage = utils.createMessageCache(e);
    cMessage.put(e.getMessageId(), cachedMessage);

    UserMessage msg = utils.createMessage(e);
    String authorid = msg.getAuthorId();

    if (!cMembers.containsKey(authorid)) {
      //create cache if not exist
      CachedUser usercache = utils.createUserCache(e, msg);

      cMembers.put(authorid, usercache);
      return;
    }

    List<UserMessage> messages = cMembers.get(authorid).getUserMessages();

    if (messages.size() >= 100) {
      messages.removeFirst();
    }

    messages.add(msg);
  }

  public Map<String, CachedMessage> getCachedMessages() {
    return cMessage;
  }
}
