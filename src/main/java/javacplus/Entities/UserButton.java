package javacplus.Entities;

import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class UserButton {
    private final String[] information;
    private final ButtonStyle style;
    
    public UserButton(String[] information, ButtonStyle style) {
        this.information = information;
        this.style = style;
    }
    
    public String[] getInformation() {
        return information;
    }

    public ButtonStyle getButtonStyle() {
        return style;
    }
}
