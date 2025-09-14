package net.JavacClassic.Entites;

import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class UserButton {

  private final ButtonInformation information;
  private final ButtonStyle style;

  public UserButton(ButtonInformation information, ButtonStyle style) {
    this.information = information;
    this.style = style;
  }

  public ButtonInformation getInformation() {
    return information;
  }

  public ButtonStyle getButtonStyle() {
    return style;
  }
}
