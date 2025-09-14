package net.JavacClassic.Commands;

import net.JavacClassic.Entites.CommandInformation;
import net.JavacClassic.Enums.CommandCategory;
import net.JavacClassic.Handlers.Command.ContextCommand;
import net.JavacClassic.Interfaces.ICommand;

public class Ping implements ICommand {

    @Override
    public CommandInformation getInformation() {
        return new CommandInformation(
                "ping",
                "Ping the bot to check if it is online.",
                CommandCategory.Owner,
                5
        );
    }

    @Override
    public void execute(ContextCommand ctx) {
        ctx.getMessage().reply("Pong amınakoyim.").queue();
    }
}
