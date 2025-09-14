package net.JavacClassic.Cache;

import net.JavacClassic.Interfaces.ICommand;

import java.util.HashMap;
import java.util.Map;

public class CCommand {
    Map<String, ICommand> commands = new HashMap<>();

    public void addCache(String name, ICommand command) {
        commands.put(name, command);
    }

    public ICommand getCache(String name) {
        return commands.getOrDefault(name, null);
    }
}
