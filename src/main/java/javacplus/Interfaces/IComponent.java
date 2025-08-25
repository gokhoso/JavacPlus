package javacplus.Interfaces;

import javacplus.Handlers.Component.ComponentContext;

public interface IComponent {
    String[] getInformation();

    void execute(ComponentContext ctx);
}
