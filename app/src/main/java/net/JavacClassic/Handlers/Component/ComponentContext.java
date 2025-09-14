package net.JavacClassic.Handlers.Component;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public class ComponentContext {

  private final GenericComponentInteractionCreateEvent event;

  public ComponentContext(GenericComponentInteractionCreateEvent event) {
    this.event = event;
  }

  public GenericComponentInteractionCreateEvent getEvent() {
    return event;
  }

  public Guild getGuild() {
    return event.getGuild();
  }
}
