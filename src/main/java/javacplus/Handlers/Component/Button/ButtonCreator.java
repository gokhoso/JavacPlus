package javacplus.Handlers.Component.Button;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Entities.UserButton;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class ButtonCreator {
    private final Logger logger = LoggerFactory.getLogger(ButtonCreator.class);
    private final ButtonRegistry bRegistry;

    public ButtonCreator(ButtonRegistry bRegistry) {
        this.bRegistry = bRegistry;
    }

    private Button getButton(String userId, String label, ButtonStyle bStyle) {
        Button button = switch (bStyle) {
            case DANGER -> Button.danger(userId, label);
            case LINK ->  Button.link(userId, label);
            case PRIMARY -> Button.primary(userId, label);
            case SECONDARY -> Button.secondary(userId, label);
            case SUCCESS -> Button.success(userId, label);
            default -> null;
        };        

        return button;
    }

    public void addUserButton(String userId, Message message,  String[] information, ButtonStyle bStyle) {
        if (userId == null || message == null || information == null || information.length < 2 || bStyle == null) {
            logger.debug("addUserButton is started with null parameters!");
            return;
        }

        bRegistry.registerUserButton(userId, new UserButton(information, bStyle));

        String label = (information.length > 0) ? information[0] : "Button";
        Button button = getButton(userId, label, bStyle);

        message.editMessageComponents().setComponents(ActionRow.of(button)).queue();
    }
}
