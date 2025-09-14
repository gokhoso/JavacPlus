package net.JavacClassic.Utils;

import io.github.cdimascio.dotenv.Dotenv;
import net.JavacClassic.Config;
import net.JavacClassic.Handlers.Command.ContextCommand;
import net.JavacClassic.Interfaces.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CategoryUtils {

    private boolean isOwner(MessageReceivedEvent event) {
    return event.getAuthor().getId().equals(Config.data.bot.owners.getFirst());
  }

  private boolean canExecuteModeration(
    ContextCommand ctx,
    MessageReceivedEvent event
  ) {
    if (!new ModerationUtils().canExecute(ctx)) {
        String moderationNotAllowedText = "Bu komutu kullanabilmek için yeterli yetkiniz yok.";
        event.getMessage().reply(moderationNotAllowedText).queue();
      return false;
    }
    return true;
  }

  public boolean isExecutable(
    ContextCommand ctx,
    ICommand command,
    MessageReceivedEvent event
  ) {
      return switch (command.getInformation().getCategory()) {
          case Owner -> isOwner(event);
          case Moderation -> canExecuteModeration(ctx, event);
          default -> true;
      };
  }
}
