package net.JavacClassic.Handlers.Component;

import io.github.cdimascio.dotenv.Dotenv;
import net.JavacClassic.Handlers.Component.Button.ButtonManager;

public class ComponentMain {

  private final String basePackage = Dotenv.load().get("BASEPACKAGE");
  private final String buttonPath = "Components.Button";

  private final ButtonManager bManager;

  public ComponentMain() {
    bManager = new ButtonManager(basePackage + "." + buttonPath); // For Reflection
  }

  public ButtonManager getButtonManager() {
    return bManager;
  }
}
