package net.JavacClassic.Entites;

public class OldUserMessages {

  private String content;
  private long timeStamp;

  public OldUserMessages(String content, long timeStamp) {
    this.content = content;
    this.timeStamp = timeStamp;
  }

  public String getContent() {
    return content;
  }

  public long getTimeStamp() {
    return timeStamp;
  }
}
