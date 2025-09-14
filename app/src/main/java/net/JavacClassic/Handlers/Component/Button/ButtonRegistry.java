package net.JavacClassic.Handlers.Component.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.JavacClassic.Entites.UserButton;
import net.JavacClassic.Interfaces.IButton;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonRegistry {

  private final Logger logger = LoggerFactory.getLogger(ButtonRegistry.class);
  private final Map<String, IButton> buttons = new HashMap<>(); // Componentname, ComponentObject
  private final Map<String, List<UserButton>> userRegistry =
    new ConcurrentHashMap<>(); // UserId, { "UserButton Entity" }
  private final ScheduledExecutorService executor =
    new ScheduledThreadPoolExecutor(3);

  public ButtonRegistry(String componentPackage) {
    autoRegister(componentPackage);
  }

  public List<UserButton> getUserRegistry(String userId) {
    return userRegistry.getOrDefault(userId, Collections.emptyList());
  }

  public IButton getButton(String name) {
    return buttons.containsKey(name) ? buttons.get(name) : null;
  }

  public void registerUserButton(String userId, UserButton userButton) {
    if (userRegistry.containsKey(userId)) {
      List<UserButton> buttons = userRegistry.get(userId);
      buttons.add(userButton);
      return;
    }

    userRegistry.put(userId, new ArrayList<>(List.of(userButton)));
    clearUser(userId, 30);
  }

  public void clearUser(String userId, int seconds) {
    executor.schedule(
      () -> {
        userRegistry.remove(userId);
      },
      seconds,
      TimeUnit.SECONDS
    );
  }

  private void autoRegister(String componentPackage) {
    final Reflections reflections = new Reflections(componentPackage);
    Set<Class<? extends IButton>> componentClasses = reflections.getSubTypesOf(
      IButton.class
    );
    for (Class<? extends IButton> componentClass : componentClasses) {
      try {
        logger.debug("Trying to add component {}", componentClass);
        final IButton component = componentClass
          .getDeclaredConstructor()
          .newInstance();
        logger.info("Registering component: {}", component.getInformation()[0]);
        buttons.put(component.getInformation()[0], component);
      } catch (Exception e) {
        logger.error("Error in autoRegister: ", e);
      }
    }
  }
}
