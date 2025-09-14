package net.JavacClassic.Systems;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.JavacClassic.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BumpReminder {

  private final ScheduledExecutorService scheduler =
    Executors.newSingleThreadScheduledExecutor();

  public void run(MessageReceivedEvent event) {
    if (
      !event.getAuthor().isBot() ||
      !event.getAuthor().getId().equals("302050872383242240")
    ) {
      return;
    }

    if (event.getMessage().getEmbeds().isEmpty()) {
      return;
    }

    String description = event
      .getMessage()
      .getEmbeds()
      .getFirst()
      .getDescription();

    if (description == null) {
      return;
    }

    if (description.contains("Öne çıkarma başarılı!")) {
      EmbedBuilder embed = new EmbedBuilder()
        .setTitle("✨ Bump Başarılı!")
        .setDescription("Sunucumuz başarıyla **bumplandı** 🎉")
        .setColor(0x5865F2)
        .addField(
          "⏳ Sonraki Bump",
          "Yaklaşık **2 saat** içinde tekrar yapman için hatırlatacağım!",
          false
        )
        .setFooter(
          event.getJDA().getSelfUser().getName() + " Bump Hatırlatıcı",
          event.getJDA().getSelfUser().getAvatarUrl()
        )
        .setTimestamp(Instant.now())
        .setThumbnail(event.getGuild().getIconUrl());

      event.getChannel().sendMessageEmbeds(embed.build()).queue();

      scheduler.schedule(
        () -> {
          EmbedBuilder reminder = new EmbedBuilder()
            .setTitle("⏰ Bump Zamanı!")
            .setDescription("Hey, tekrar bump atabilirsiniz! 🚀")
            .setColor(0x57F287) // yeşil
            .setTimestamp(Instant.now());

          event
            .getGuild()
            .getTextChannelById(Config.data.channels.general)
            .sendMessageEmbeds(reminder.build())
            .queue();
        },
        2,
        TimeUnit.HOURS
      );
    }
  }
}
