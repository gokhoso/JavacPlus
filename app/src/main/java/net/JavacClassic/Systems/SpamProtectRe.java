package net.JavacClassic.Systems;

import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Entites.UserMessage;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SpamProtectRe {
    private final CUser cUser;
    private final Logger logger = LoggerFactory.getLogger(SpamProtectRe.class);

    public SpamProtectRe(CUser cUser) {
        this.cUser = cUser;
    }

    public void isSpamming(MessageReceivedEvent event) {
        final List<UserMessage> messages = cUser.getCache(event.getAuthor().getId()).getUserMessages();

        if (messages.isEmpty() || messages.size() < 5) return;

        List<String> middle5char = new ArrayList<>();

        messages.subList(messages.size() - 5, messages.size()).forEach(message -> {
            String text = message.getContent();
            int len = text.length();
            int n = 4;

            if (len < 3) return;

            int mid = len / 2;
            int half = n / 2;
            int start = Math.max(mid - half, 0);
            int end = Math.min(start + 5, len);

            String middle = text.substring(start, end);
            middle5char.add(middle);
        });

        if (middle5char.size() < 5) return;

        for (int i = 2; i < middle5char.size(); i++) {
            String first = middle5char.get(i - 2);
            String second = middle5char.get(i - 1);
            String third = middle5char.get(i);

            if (!messages.isEmpty() && Objects.equals(first, second) && Objects.equals(second, third)) {
                logger.info("SPAM TESPIT EDILDI!");
                protect(event, messages.subList(messages.size() - 5, messages.size()));
            }
          }
    }

    public void protect(MessageReceivedEvent event, List<UserMessage> messages) {
        Member member = event.getMember();
        if (member == null) return;

        try {
            member.timeoutFor(3, TimeUnit.HOURS).queue();
        } catch(HierarchyException ignored) {}

        List<String> ids = messages.stream().map(UserMessage::getMessageId).toList();
        event.getChannel().asTextChannel().deleteMessagesByIds(ids).queue();

        messages.clear();
    }
}
