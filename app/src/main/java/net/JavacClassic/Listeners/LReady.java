package net.JavacClassic.Listeners;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LReady extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(LReady.class);
  @Override
  public void onReady(@Nonnull ReadyEvent event) {
      logger.info("Bot is successfully started!");
  }
}
