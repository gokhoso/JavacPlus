package javacplus.Listeners;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Handlers.Database.DatabaseHandler;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerMember extends ListenerAdapter {
    private final DatabaseHandler database;
    private final Logger logger = LoggerFactory.getLogger(ListenerMember.class);
    private final String query = "SELECT doorChannel, memberRole FROM SERVERS WHERE serverID = ?";
    private final String welcomeMsg = "Selam, raid sebepleriyle  kayıt sistemindeyiz biraz beklemen gerekecek...";

    public ListenerMember(DatabaseHandler database) {
        this.database = database;
    }

    private String[] getIDs(String serverID) {
        try (var connection = database.getConnection("ServerDatabase")) {
            try (var pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, serverID);
                try (var rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String doorChannel = rs.getString("doorChannel");
                        String memberRole = rs.getString("memberRole");

                        if (doorChannel != null && memberRole != null) {
                            return new String[] { doorChannel, memberRole };
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.warn("Error in ListenerMember: {}", e);
        }
        logger.warn("ID's NULL!");
        return null;
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        String[] ids = getIDs(event.getGuild().getId());
        if (ids == null) {
            logger.warn("IDS NULL!");
            return;
        }
        TextChannel channel = event.getGuild().getTextChannelById(ids[0]);
        Role role = event.getGuild().getRoleById(ids[1]);

        if (channel == null || role == null) {
            logger.warn("Channel or Role NULL!");
            return;
        }

        try {
            logger.info("Adding role and sending message...");
            event.getGuild().addRoleToMember(event.getMember(), role).queue();
            channel.sendMessage(welcomeMsg + event.getMember().getAsMention()).queueAfter(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error in onGuildMemberJoin: {}", e);
        }

    }
}
