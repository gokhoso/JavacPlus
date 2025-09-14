package net.JavacClassic.Commands;

import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Entites.CachedUser;
import net.JavacClassic.Entites.CommandInformation;
import net.JavacClassic.Entites.UserMessage;
import net.JavacClassic.Enums.CommandCategory;
import net.JavacClassic.Handlers.Command.ContextCommand;
import net.JavacClassic.Interfaces.ICommand;

import java.util.stream.Collectors;

public class GetCache implements ICommand {
    @Override
    public CommandInformation getInformation() {
        return new CommandInformation("getcache", "returns cache", CommandCategory.Owner, 5);
    }

    @Override
    public void execute(ContextCommand ctx) {
        CUser cUser = ctx.getcUser();
        if (ctx.getArgs().length != 2) return;
        CachedUser cachedUser = cUser.getCache(ctx.getArgs()[1]);
        String msg = cachedUser.getUserMessages().stream().map(UserMessage::getContent).collect(Collectors.joining("\n"));
        System.out.println("EXECUTINNGGGGG");
        ctx.sendMessage(msg);
    }
}
