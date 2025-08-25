package javacplus.Components.Button;

import javacplus.Handlers.Component.ComponentContext;
import javacplus.Interfaces.IComponent;

public class SecurityButton implements IComponent {

    @Override
    public String[] getInformation() {
        return new String[] {
                "securitybutton"
        };
    }

    @Override
    public void execute(ComponentContext ctx) {
        ctx.getEvent().getChannel().sendMessage("Allah yok!").queue();
    }
}
