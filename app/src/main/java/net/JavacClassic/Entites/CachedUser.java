package net.JavacClassic.Entites;

import java.util.ArrayList;
import java.util.List;

public class CachedUser {

  private final String userid;
  private final String username;
  private final String avatarUrl;
  private final String guildName;
  private final List<UserMessage> userMessages = new ArrayList<>();

  public CachedUser(
    String userid,
    String username,
    String avatarUrl,
    String guildName,
    UserMessage userMessage
  ) {
    this.userid = userid;
    this.username = username;
    this.avatarUrl = avatarUrl;
    this.guildName = guildName;
    this.userMessages.add(userMessage);
  }

  public String getGuildName() {
    return guildName;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public String getUserId() {
    return userid;
  }

  public String getUsername() {
    return username;
  }

  public List<UserMessage> getUserMessages() {
    return userMessages;
  }
}
