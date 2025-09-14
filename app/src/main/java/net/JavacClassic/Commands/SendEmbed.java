package net.JavacClassic.Commands;

import net.JavacClassic.Entites.CommandInformation;
import net.JavacClassic.Enums.CommandCategory;
import net.JavacClassic.Handlers.Command.ContextCommand;
import net.JavacClassic.Interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class SendEmbed implements ICommand {

  @Override
  public CommandInformation getInformation() {
      return new CommandInformation(
              "sendembed",
              "Sends a embed message to the channel.",
              CommandCategory.Owner,
              5
      );
  }

  @Override
  public void execute(ContextCommand ctx) {
    String desc = String.join(" ", ctx.getArgs()).substring(11);
    EmbedBuilder embed = new EmbedBuilder().setDescription(desc);
    ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
  }
}
