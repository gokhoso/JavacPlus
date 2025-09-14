package net.JavacClassic.Entites;

public class UserMessage {

  private final String content;
  private final String authorId;
  private final String channelId;
  private final String messageId;

  public UserMessage(
    String content,
    String authorId,
    String channelId,
    String messageId
  ) {
    this.content = content;
    this.authorId = authorId;
    this.channelId = channelId;
    this.messageId = messageId;
  }

  public String getMessageId() {
    return messageId;
  }

  public String getChannelId() {
    return channelId;
  }

  public String getContent() {
    return content;
  }

  public String getAuthorId() {
    return authorId;
  }
}
