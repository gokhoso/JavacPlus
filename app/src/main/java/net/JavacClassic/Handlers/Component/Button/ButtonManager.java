package net.JavacClassic.Handlers.Component.Button;

public class ButtonManager {

  private final ButtonRegistry bRegistry;
  private final ButtonCreator bCreator;

  public ButtonManager(String reflectionPath) {
    this.bRegistry = new ButtonRegistry(reflectionPath);
    this.bCreator = new ButtonCreator(bRegistry);
  }

  public ButtonRegistry getRegistry() {
    return bRegistry;
  }

  public ButtonCreator getCreator() {
    return bCreator;
  }
}
