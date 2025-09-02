package javacplus.Handlers.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import javacplus.Handlers.Component.ComponentMain;
import javacplus.Handlers.Database.DatabaseHandler;
import javacplus.Interfaces.ICommand;
import javacplus.Utils.CategoryUtils;
import javacplus.Handlers.Security.CooldownManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHandler {
    private final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final CooldownManager cooldown = new CooldownManager();
    private final Map<String, ICommand> commands = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public CommandHandler() {
        if (commands == null || !commands.isEmpty()) {
            return;
        }

        final Reflections reflections = new Reflections("javacplus.Commands");
        Set<Class<? extends ICommand>> commandClasses = reflections.getSubTypesOf(ICommand.class);
        for (Class<? extends ICommand> commandClass : commandClasses) {
            try {
                logger.debug("Trying to add command {}", commandClass);
                final ICommand command = commandClass.getDeclaredConstructor().newInstance();
                logger.info("Registering command: {}", command.getInformation()[0]);
                commands.put(command.getInformation()[0], command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean executable(MessageReceivedEvent event) {
        String msgContent = event.getMessage().getContentRaw();
        String prefix = Dotenv.load().get("PREFIX");
        boolean isBot = event.getAuthor().isBot();

        return !isBot && msgContent.length() > 2 + prefix.length() && msgContent.startsWith(prefix) ? true : false;
    }

    private boolean isOnCooldown(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();

        if (cooldown.isRateLimited(userId))
            return true;

        if (cooldown.hasCooldown(userId)) {
            cooldown.addRatelimit(userId, cooldown.getCooldown(userId));
            event.getMessage().reply("Bu komutu kullanabilmek için beklemeniz gerekiyor.").queue();
        }

        return cooldown.hasCooldown(userId);
    }

    public void execute(MessageReceivedEvent event, ComponentMain componentMain, DatabaseHandler database) {
        if (!executable(event) || isOnCooldown(event))
            return;

        final Dotenv dotenv = Dotenv.load();
        String prefix = dotenv.get("PREFIX");

        if (prefix == null)
            prefix = "j.";

        final String[] args = event.getMessage().getContentRaw().replace(prefix, "").trim().split("\\s+");

        logger.debug("Args0 : {}", args[0]);
        logger.debug("Commands: {}", new ArrayList<String>(commands.keySet()));

        if (!commands.containsKey(args[0])) {
            logger.debug("args[0] ({}) does not avaliable on commands", args[0]);
            cooldown.addCooldown(event.getAuthor().getId(), 10);
            event.getMessage().reply("<a:tetono:1399040886142144582> öyle olmaz! yok öyle komut!").queue(
                    msg -> {
                        scheduler.schedule(() -> {
                            msg.delete().queue();
                        }, 10, TimeUnit.SECONDS);
                    });
            return;
        }

        final ICommand command = commands.get(args[0]);

        if (command == null) {
            logger.debug("COMMAND IS NULL!");
            return;
        }

        final ContextCommand ctx = new ContextCommand(event, componentMain, database);

        final boolean categoryExecutable = new CategoryUtils().isExecutable(ctx, command, event);
        if (!categoryExecutable)
            return;

        logger.info("Executing {} by {}", command.getInformation()[0], event.getAuthor().getName());
        command.execute(ctx);
        cooldown.addCooldown(ctx.getAuthorId(), Integer.parseInt(command.getInformation()[3]));
    }
}
