package javacplus.Utils;

import io.github.cdimascio.dotenv.Dotenv;
import javacplus.Handlers.Command.ContextCommand;
import javacplus.Interfaces.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CategoryUtils {
    private final String moderationNotAllowedText = "Bu komutu kullanabilmek için yeterli yetkiniz yok.";

    private boolean isOwner(MessageReceivedEvent event) {
        return event.getAuthor().getId().equals(Dotenv.load().get("OWNERID"));
    }

    private boolean canExecuteModeration(ContextCommand ctx, MessageReceivedEvent event) {
        if (!new ModerationUtils().canExecute(ctx)) {
            event.getMessage().reply(moderationNotAllowedText).queue();
            return false;
        }
        return true;
    }

    public boolean isExecutable(ContextCommand ctx, ICommand command, MessageReceivedEvent event) {
        switch (command.getInformation()[2]) {
            case "owner":
                return isOwner(event);
            case "moderation":
                return canExecuteModeration(ctx, event);
            default:
                return true;
        }
    }
}
