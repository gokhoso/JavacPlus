package net.JavacClassic.Entites;

import java.time.Instant;

public class CachedMessage {

  private final String msgId;
  private final Instant time;
  private final String authorId;

  public CachedMessage(String msgId, String authorId, Instant time) {
    this.msgId = msgId;
    this.time = time;
    this.authorId = authorId;
  }

  public Instant getCreated() {
    return time;
  }

  public String getId() {
    return msgId;
  }

  public String getAuthorId() {
    return authorId;
  }
}
