package net.JavacClassic.Systems;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.JavacClassic.Entites.OldUserMessages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SpamProtect {

  private final ConcurrentHashMap<String, Deque<OldUserMessages>> messages =
    new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Long> blackList =
    new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler =
    Executors.newScheduledThreadPool(1);

  private final long timeWindow = 10_000; // 10 saniye
  private final int maxMessages = 4;
  private final long blacklistTimeout = 30_000; // 30 saniye

  public SpamProtect() {
    // Blacklist cleanup task
    scheduler.scheduleAtFixedRate(
      () -> {
        long now = System.currentTimeMillis();
        blackList
          .entrySet()
          .removeIf(entry -> now - entry.getValue() > blacklistTimeout);
      },
      5,
      5,
      TimeUnit.SECONDS
    );
  }

  public void addMessage(String userID, String message) {
    messages.putIfAbsent(userID, new ConcurrentLinkedDeque<>());
    Deque<OldUserMessages> userMessages = messages.get(userID);

    // Son mesaj eklemeden önce deque limit kontrolü
    if (userMessages.size() >= maxMessages) {
      userMessages.pollFirst(); // en eskiyi at
    }

    userMessages.addLast(
      new OldUserMessages(message, System.currentTimeMillis())
    );
  }

  private boolean isShortMessageSpamming(String userID) {
    Deque<OldUserMessages> userMessages = messages.get(userID);
    if (userMessages == null) return false;

    long now = System.currentTimeMillis();
    int recentShortCount = 0;

    for (OldUserMessages msg : userMessages) {
      if (now - msg.getTimeStamp() > timeWindow) continue;
      recentShortCount++;
    }
    return recentShortCount >= maxMessages;
  }

  private boolean isContentSimilar(String a, String b) {
    if (a == null || b == null) return false;
    if (a.equals(b)) return true;

    int minLen = 3;
    if (a.length() < minLen || b.length() < minLen) return false;

    // İlk 3 veya son 3 karakter kontrolü
    if (
      a.startsWith(b.substring(0, minLen)) ||
      a.endsWith(b.substring(b.length() - minLen))
    ) return true;

    // Ortada ortak substring
    return hasCommonSubstring(a, b, minLen);
  }

  private boolean hasCommonSubstring(String a, String b, int minLen) {
    for (int i = 0; i <= a.length() - minLen; i++) {
      String subA = a.substring(i, i + minLen);
      for (int j = 0; j <= b.length() - minLen; j++) {
        if (subA.equals(b.substring(j, j + minLen))) return true;
      }
    }
    return false;
  }

  private boolean isRepeatingMessages(String userID) {
    if (blackList.containsKey(userID)) return true;

    Deque<OldUserMessages> userMessages = messages.get(userID);
    if (userMessages == null || userMessages.size() < maxMessages) return false;

    OldUserMessages first = userMessages.peekFirst();
    for (OldUserMessages msg : userMessages) {
      if (isContentSimilar(first.getContent(), msg.getContent())) {
        blackList.put(userID, System.currentTimeMillis());
        return true;
      }
    }
    return false;
  }

  public boolean shouldLimit(MessageReceivedEvent event) {
    String userID = event.getAuthor().getId();
    return isRepeatingMessages(userID) && isShortMessageSpamming(userID);
  }

  public void handleMessage(MessageReceivedEvent event) {
    String userID = event.getAuthor().getId();
    String message = event.getMessage().getContentRaw();

    addMessage(userID, message);

    if (shouldLimit(event)) {
      limitMember(event);
    }
  }

  private void limitMember(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;
    Member member = event.getMember();
    if (member == null) return;

    String userID = member.getId();
    if (!blackList.containsKey(userID)) return;

    member.timeoutFor(1, TimeUnit.HOURS).queue();
    event
      .getGuild()
      .getTextChannelById(1399017428092194837L)
      .sendMessage(
        member.getAsMention() + " spam sebebinden timeoutlandı @here"
      )
      .queue(
        // delete last 30 messages
        s -> {
          event
            .getMessage()
            .getChannel()
            .asTextChannel()
            .sendMessage(
              member.getAsMention() + " spam sebebiyle 1 saaat engellendi."
            )
            .queue();
          event
            .getChannel()
            .getHistory()
            .retrievePast(10)
            .queue(messages -> {
              List<Message> userMessages = messages
                .stream()
                .filter(msg -> msg.getAuthor().getId().equals(userID))
                .toList();
              event
                .getGuild()
                .getTextChannelById(event.getChannel().getId())
                .deleteMessages(userMessages)
                .queue();
            });
        }
      );
  }
}
