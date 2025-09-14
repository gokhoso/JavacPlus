package net.JavacClassic.Handlers.Component.Button;

import java.util.ArrayList;
import java.util.List;
import net.JavacClassic.Entites.ButtonInformation;
import net.JavacClassic.Entites.UserButton;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonCreator {

  private final Logger logger = LoggerFactory.getLogger(ButtonCreator.class);
  private final ButtonRegistry bRegistry;

  public ButtonCreator(ButtonRegistry bRegistry) {
    this.bRegistry = bRegistry;
  }

  private Button createButton(String id, String label, ButtonStyle bStyle) {
    Button button =
      switch (bStyle) {
        case DANGER -> Button.danger(id, label);
        case LINK -> Button.link(id, label);
        case PRIMARY -> Button.primary(id, label);
        case SECONDARY -> Button.secondary(id, label);
        case SUCCESS -> Button.success(id, label);
        default -> null;
      };

    return button;
  }

  public void createUserButton(
    String userId,
    Message message,
    ButtonInformation information,
    ButtonStyle bStyle
  ) {
    if (
      userId == null || message == null || information == null || bStyle == null
    ) {
      logger.debug("addUserButton is started with null parameters!");
      return;
    }

    bRegistry.registerUserButton(userId, new UserButton(information, bStyle));
    List<UserButton> userButtons = bRegistry.getUserRegistry(userId);
    List<Button> buttons = new ArrayList<>();

    for (UserButton btn : userButtons) {
      String btnId = btn.getInformation().getId();
      String btnLabel = btn.getInformation().getLabel();
      Button button = createButton(btnId, btnLabel, btn.getButtonStyle());
      buttons.add(button);
    }

    if (buttons.isEmpty()) {
      logger.debug("Buttons is null");
      return;
    }

    message
      .editMessageComponents()
      .setComponents(ActionRow.of(buttons))
      .queue();
  }
}
