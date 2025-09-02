package javacplus.Commands;

import javacplus.Handlers.Command.ContextCommand;
import javacplus.Interfaces.ICommand;

public class Ping implements ICommand {

    @Override
    public String[] getInformation() {
        return new String[] {
                "ping",
                "Ping the bot to check if it is online.",
                "owner",
                "5"
        };
    }

    @Override
    public void execute(ContextCommand ctx) {
        ctx.getMessage().reply("Pong amınakoyim.").queue();
    }
}
