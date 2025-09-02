package javacplus.Interfaces;

import javacplus.Handlers.Component.ComponentContext;

public interface IButton {
    String[] getInformation();

    void execute(ComponentContext ctx);
}
