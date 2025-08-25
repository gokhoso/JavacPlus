package javacplus.Handlers.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Interfaces.IComponent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class ComponentHandler {
    private final Logger logger = LoggerFactory.getLogger(ComponentHandler.class);
    private final Map<String, IComponent> components = new HashMap<>();
    private Map<String, String> UserComponents = new HashMap<>();

    public ComponentHandler() {
        final Reflections reflections = new Reflections("javacplus.Components");
        Set<Class<? extends IComponent>> componentClasses = reflections.getSubTypesOf(IComponent.class);
        for (Class<? extends IComponent> componentClass : componentClasses) {
            try {
                logger.debug("Trying to add component {}", componentClass);
                final IComponent component = componentClass.getDeclaredConstructor().newInstance();
                logger.info("Registering component: {}", component.getInformation()[0]);
                components.put(component.getInformation()[0], component);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerUserComponent(String userid, String componentId) {
        UserComponents.put(userid, componentId);
    }

    public Optional<String> getUserComponent(String userid) {
        if (UserComponents.containsKey(userid)) {
            return Optional.of(UserComponents.get(userid));
        }

        return Optional.empty();
    }

    public Optional<IComponent> getComponent(String name) {
        if (components.containsKey(name)) {
            return Optional.of(components.get(name));
        }
        return Optional.empty();
    }

    public void createButtonPrimary(Message message, String userid, String buttonId, String buttonLabel) {
        message.editMessageComponents().setComponents(ActionRow.of(
                Button.primary(buttonId, buttonLabel))).queue(
                        _ -> {
                            registerUserComponent(userid, buttonId);
                        });
    }

    public void createStringMenu(Message message, String userid, String menuId, String menuLabel, String... options) {
        var menu = StringSelectMenu.create(menuId)
                .setPlaceholder(menuLabel)
                .setMinValues(1)
                .setMaxValues(1);

        for (String option : options) {
            menu.addOption(option, option.toLowerCase());
        }

        message.editMessageComponents().setComponents(ActionRow.of(menu.build())).queue(
                _ -> {
                    registerUserComponent(userid, menuId);
                });
    }
}
