package net.JavacClassic.Handlers.Command;

import net.JavacClassic.Cache.CCommand;
import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Config;
import net.JavacClassic.Handlers.Component.ComponentMain;
import net.JavacClassic.Interfaces.ICommand;
import net.JavacClassic.Security.CooldownManager;
import net.JavacClassic.Utils.CategoryUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CommandMain {
    private final CCommand cCommand;
    private final Logger logger = LoggerFactory.getLogger(CommandMain.class);
    private final CommandSecurity security;
    private final CooldownManager cooldownManager;

    private void load(String basePackage) {
        final Reflections reflections = new Reflections(basePackage);

        Set<Class<? extends ICommand>> commandClasses = reflections.getSubTypesOf(ICommand.class);
        for (Class<? extends ICommand> commandClass : commandClasses) {
            try {
                logger.debug("Trying to add command {}", commandClass);
                final ICommand command = commandClass.getDeclaredConstructor().newInstance();
                logger.info("Registering command: {}", command.getInformation().getName());
                cCommand.addCache(command.getInformation().getName(), command);
            } catch (Exception e) {
                logger.error("Error while loading commands: ", e);
            }
        }
    }

    public CommandMain(CCommand cCommand, CooldownManager cooldownManager) {
        this.cCommand = cCommand;
        this.cooldownManager = cooldownManager;
        this.security = new CommandSecurity(cooldownManager);

        load(Config.data.bot.basePackage + ".Commands");
    }

    public void execute(MessageReceivedEvent event, ComponentMain componentMain, CUser cUser) {
        if (!security.executable(event) || security.isOnCooldown(event)) return;

        String prefix = Config.data.bot.prefix;
        final String[] args = event.getMessage().getContentRaw().replace(prefix, "").trim().split("\\s+");

        if (cCommand.getCache(args[0]) == null) {
            logger.debug("args[0] ({}) does not avaliable on commands", args[0]);
            cooldownManager.addCooldown(event.getAuthor().getId(), 10);
            return;
        }

        final ICommand command = cCommand.getCache(args[0]);
        if (command == null) {
            logger.debug("COMMAND IS NULL!");
            return;
        }

        final ContextCommand ctx = new ContextCommand(event, componentMain, cUser);

        final boolean categoryExecutable = new CategoryUtils().isExecutable(ctx, command, event);

        if (!categoryExecutable) {
            logger.info("Category not executable!");
            return;
        }

        logger.info("Executing {} by {}", command.getInformation().getName(), event.getAuthor().getName());
        command.execute(ctx);

        cooldownManager.addCooldown(ctx.getAuthorId(), command.getInformation().getCooldown());


    }
}
