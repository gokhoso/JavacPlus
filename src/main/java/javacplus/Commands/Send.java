package javacplus.Commands;

import javacplus.Handlers.Command.ContextCommand;
import javacplus.Interfaces.ICommand;

public class Send implements ICommand {

    @Override
    public String[] getInformation() {
        return new String[] {
                "send",
                "Sends a message to the channel.",
                "owner",
                "5"
        };
    }

    @Override
    public void execute(ContextCommand ctx) {
        ctx.sendMessage(ctx.getMessageContent());
    }
}
