package net.JavacClassic.Entites;

public class ButtonInformation {

  private final String id;
  private final String label;
  private final String uniqueMessageId;

  public ButtonInformation(String id, String label, String uniqueMessageId) {
    this.id = id;
    this.label = label;
    this.uniqueMessageId = uniqueMessageId;
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public String getUniqueMessageId() {
    return uniqueMessageId;
  }
}
