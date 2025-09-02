package javacplus.Components.StringMenu;

import javacplus.Handlers.Component.ComponentContext;
import javacplus.Interfaces.IButton;

public class TestStringMenu implements IButton {

    @Override
    public String[] getInformation() {
        return new String[] {
                "teststringmenu"
        };
    }

    @Override
    public void execute(ComponentContext ctx) {
        ctx.getEvent().getChannel().sendMessage("bravo").queue();
    }

}
