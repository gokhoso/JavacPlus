package net.JavacClassic.Systems;

import io.github.cdimascio.dotenv.Dotenv;

import java.awt.Color;
import java.time.Instant;

import net.JavacClassic.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class WelcomeSystem {
    private final Dotenv dotenv = Dotenv.load();

    private void addRole(GuildMemberJoinEvent event) {
        Role role = event.getGuild().getRoleById(Config.data.roles.member);

        if (role == null) {
            return;
        }

        event.getGuild().addRoleToMember(event.getMember(), role).queue();
    }

    private TextChannel getGeneral(Guild guild) {
        return guild.getTextChannelById(Config.data.channels.general);
    }

    private EmbedBuilder getEmbed(GuildMemberJoinEvent event) {
        event.getGuild().loadMembers();
        int memberCount = event.getGuild().getMemberCount();
        String rulesMention = String.format("<#%s>", Config.data.channels.rule);
        String pilavMention = String.format("<#%s>", Config.data.channels.rice);

        return new EmbedBuilder().setAuthor(event.getUser().getName(), null, event.getUser().getEffectiveAvatarUrl()).setColor(Color.MAGENTA).setTitle("🌸 LINXUU'ya Hoş Geldin, " + event.getUser().getName() + "!").setThumbnail(event.getUser().getEffectiveAvatarUrl()).setImage("https://cdn.discordapp.com/attachments/1395762929286254785/1413486435390197770/image.png").setDescription("🎉 " + event.getUser().getAsMention() + " aramıza katıldı!\n\n" + "📜 Kuralları okumayı unutma, iyi vakit geçir! 💖").addField("👥 Senin Sıran:", memberCount + ". üye!", true).addField("📌 Faydalı Kanallar:", "👉 " + pilavMention + "\n👉" + rulesMention, false).setFooter("LINXUU Ailesi Seni Bekliyordu ✨", event.getGuild().getIconUrl()).setTimestamp(Instant.now());
    }

    public void start(GuildMemberJoinEvent event) {
        addRole(event);

        TextChannel generalChannel = getGeneral(event.getGuild());
        EmbedBuilder embed = getEmbed(event);

        generalChannel.sendMessageEmbeds(embed.build()).queue();
    }
}
