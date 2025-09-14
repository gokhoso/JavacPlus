package net.JavacClassic.Commands;

import net.JavacClassic.Entites.CommandInformation;
import net.JavacClassic.Enums.CommandCategory;
import net.JavacClassic.Handlers.Command.ContextCommand;
import net.JavacClassic.Interfaces.ICommand;

public class Send implements ICommand {

  @Override
  public CommandInformation getInformation() {
    return new CommandInformation(
            "send",
            "Sends a message to the channel.",
            CommandCategory.Owner,
            5
    );
  }

  @Override
  public void execute(ContextCommand ctx) {
    ctx.sendMessage(ctx.getMessageContent());
  }
}
