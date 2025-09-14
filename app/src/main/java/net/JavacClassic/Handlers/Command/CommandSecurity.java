package net.JavacClassic.Handlers.Command;

import net.JavacClassic.Config;
import net.JavacClassic.Security.CooldownManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSecurity {
    private final CooldownManager cooldown;

    public CommandSecurity(CooldownManager cooldownManager) {
        this.cooldown = cooldownManager;
    }

    public boolean executable(MessageReceivedEvent event) {
        String msgContent = event.getMessage().getContentRaw();
        String prefix = Config.data.bot.prefix;
        boolean isBot = event.getAuthor().isBot();

        return !isBot &&
                msgContent.length() > 2 + prefix.length() &&
                msgContent.startsWith(prefix);
    }

    public boolean isOnCooldown(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();

        if (cooldown.isRateLimited(userId)) return true;

        if (cooldown.hasCooldown(userId)) {
            cooldown.addRatelimit(userId, cooldown.getCooldown(userId));
            event
                    .getMessage()
                    .reply("Bu komutu kullanabilmek için beklemeniz gerekiyor.")
                    .queue();
        }

        return cooldown.hasCooldown(userId);
    }

}
