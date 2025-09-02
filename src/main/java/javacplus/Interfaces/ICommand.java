package javacplus.Interfaces;

import javacplus.Handlers.Command.ContextCommand;

public interface ICommand {
    String[] getInformation();

    void execute(ContextCommand ctx);
}
