package javacplus.Components.Button;

import javacplus.Handlers.Component.ComponentContext;
import javacplus.Interfaces.IButton;

public class SecurityButton implements IButton {

    @Override
    public String[] getInformation() {
        return new String[] {
                "securitybutton"
        };
    }

    @Override
    public void execute(ComponentContext ctx) {
        ctx.getEvent().getChannel().sendMessage("SATA ANDAGIII").queue();
    }
}
