package net.JavacClassic.Commands;

import java.util.concurrent.TimeUnit;

import net.JavacClassic.Entites.CommandInformation;
import net.JavacClassic.Enums.CommandCategory;
import net.JavacClassic.Handlers.Command.ContextCommand;
import net.JavacClassic.Interfaces.ICommand;
import net.dv8tion.jda.api.entities.User;

public class Ban implements ICommand {

  @Override
  public CommandInformation getInformation() {
      return new CommandInformation(
              "ban",
              "BAN.",
              CommandCategory.Moderation,
              5
      );
  }

  @Override
  public void execute(ContextCommand ctx) {
    if (ctx.getMessage().getMentions().getUsers().size() == 1) {
      User user = ctx.getMessage().getMentions().getUsers().getFirst();
      ctx.getGuild().ban(user, 0, TimeUnit.SECONDS).queue();
      ctx.sendMessage(
        "<a:explosion:1399041244616982548> " +
        user.getAsMention() +
        " bum oldu."
      );
    }
  }
}
