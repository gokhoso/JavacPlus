package net.JavacClassic.Listeners;

import javax.annotation.Nonnull;
import net.JavacClassic.Systems.GuildLogger;
import net.JavacClassic.Systems.WelcomeSystem;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LMember extends ListenerAdapter {

  private final WelcomeSystem welcomeSystem = new WelcomeSystem();
  private final GuildLogger logger = new GuildLogger();

  @Override
  public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
    welcomeSystem.start(event);
    logger.startMemberJoined(event);
  }
}
