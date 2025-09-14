package net.JavacClassic.Systems;

import java.awt.Color;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import net.JavacClassic.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class GuildLogger {

  private String getDate(GuildMemberJoinEvent event) {
    String accountAgeYears = Long.toString(
      ChronoUnit.YEARS.between(
        event.getUser().getTimeCreated().toLocalDate(),
        LocalDate.now()
      )
    );

    String accountAgeDays = Long.toString(
      ChronoUnit.DAYS.between(
        event.getUser().getTimeCreated().toLocalDate(),
        LocalDate.now()
      )
    );

    String accountAgeHours = Long.toString(
      ChronoUnit.DAYS.between(
        event.getUser().getTimeCreated().toLocalDate(),
        LocalDate.now()
      )
    );

      return String.format(
      "%s yil %s gun %s saat",
      accountAgeYears,
      accountAgeDays,
      accountAgeHours
    );
  }

  private EmbedBuilder getEmbed(GuildMemberJoinEvent event) {
    event.getGuild().loadMembers().get();
    int memberCount = event.getGuild().getMemberCount();
    String accountGuildDays = event
      .getMember()
      .getTimeJoined()
      .toLocalDate()
      .toString();

    return new EmbedBuilder()
      .setAuthor(
        event.getUser().getName(),
        null,
        event.getUser().getEffectiveAvatarUrl()
      )
      .setColor(Color.MAGENTA)
      .setTitle("LINXUU'ya, " + event.getUser().getName() + "Giriş yaptı!")
      .setThumbnail(event.getUser().getEffectiveAvatarUrl())
      .setDescription(event.getUser().getAsMention() + " Giriş yaptı.")
      .addField(
        "Toplam Üye: ",
        "**" + Integer.toString(memberCount) + "**",
        true
      )
      .addField("Hesap yaşı: ", "**" + getDate(event), false)
      .addField("Sunucuya katılma tarihi: ", accountGuildDays, false)
      .setTimestamp(Instant.now());
  }

  public void startMemberJoined(GuildMemberJoinEvent event) {
    EmbedBuilder embed = getEmbed(event);
    TextChannel logchannel = event
      .getGuild()
      .getTextChannelById(Config.data.channels.log);

      assert logchannel != null;
      logchannel.sendMessageEmbeds(embed.build()).queue();
  }
}
