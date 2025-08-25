package javacplus.Commands;

import java.util.concurrent.TimeUnit;

import javacplus.Handlers.Command.ContextCommand;
import javacplus.Interfaces.ICommand;
import net.dv8tion.jda.api.entities.User;

public class Ban implements ICommand {

    @Override
    public String[] getInformation() {
        return new String[] {
                "ban",
                "Ban a user from the server",
                "moderation",
                "5"
        };
    }

    @Override
    public void execute(ContextCommand ctx) {
        if (ctx.getMessage().getMentions().getUsers().size() == 1) {
            User user = ctx.getMessage().getMentions().getUsers().get(0);
            ctx.getGuild().ban(user, 0, TimeUnit.SECONDS).queue();
            ctx.sendMessage("<a:explosion:1399041244616982548> " + user.getAsMention() + " bum oldu.");
        }
    }
}
