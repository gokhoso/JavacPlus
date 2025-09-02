package javacplus.Handlers.Component.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Entities.UserButton;
import javacplus.Interfaces.IButton;

public class ButtonRegistry {
    private final Logger logger = LoggerFactory.getLogger(ButtonRegistry.class);
    private final Map<String, IButton> buttons = new HashMap<>(); // Componentname, ComponentObject
    private final Map<String, UserButton> userRegistry = new HashMap<>(); // UserId, { "UserButton Entity" }

    public ButtonRegistry(String componentPackage) {
        autoRegister(componentPackage);
    }

    public UserButton getUserRegistry(String userId) {
        return userRegistry.containsKey(userId) ? userRegistry.get(userId) : null;
    }

    public IButton getButton(String name) {
        return buttons.containsKey(name) ? buttons.get(name) : null;
    }

    public void registerUserButton(String userId, UserButton userButton) {
        userRegistry.put(userId, userButton);
    }

    private void autoRegister(String componentPackage) {
        final Reflections reflections = new Reflections(componentPackage);
        Set<Class<? extends IButton>> componentClasses = reflections.getSubTypesOf(IButton.class);
        for (Class<? extends IButton> componentClass : componentClasses) {
            try {
                logger.debug("Trying to add component {}", componentClass);
                final IButton component = componentClass.getDeclaredConstructor().newInstance();
                logger.info("Registering component: {}", component.getInformation()[0]);
                buttons.put(component.getInformation()[0], component);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
