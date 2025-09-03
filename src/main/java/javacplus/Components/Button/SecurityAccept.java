package javacplus.Components.Button;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Handlers.Component.ComponentContext;
import javacplus.Interfaces.IButton;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class SecurityAccept implements IButton {
    private final Logger logger = LoggerFactory.getLogger(SecurityAccept.class);
    private final String doorInsertQuery =
     """
        INSERT INTO SERVERS (serverID, doorChannel, memberRole)
        VALUES (?, ?, ?)
        ON CONFLICT(serverID) DO UPDATE SET doorChannel = excluded.doorChannel, memberRole = excluded.memberRole
    """;

    @Override
    public String[] getInformation() {
        return new String[] {
                "securityAccept"
        };
    }

    private void addChannelToDatabase(ComponentContext ctx, String channelId, String roleId ) {
        try (var connection = ctx.getDatabase().getConnection("ServerDatabase")) {
            try (var pstmt = connection.prepareStatement(doorInsertQuery)) {
                pstmt.setString(1, ctx.getGuild().getId());
                pstmt.setString(2, channelId);
                pstmt.setString(3, roleId);
                pstmt.executeUpdate();
                logger.debug("Inserted/Updated doorChannel and memberRole for serverID {}", ctx.getGuild().getId());
                ctx.getEvent().getChannel().sendMessage("Channel succesfully Created!").queue();
            }
        } catch (SQLException e) {
            logger.error("SQLEXCEPTION: {}", e);
            ctx.getEvent().getChannel().sendMessage("Unkown error occur!").queue();
        }
    }

private void upsertPermissions(ComponentContext ctx, TextChannel channel, Role role) {
    ctx.getGuild().getTextChannels().forEach(cn -> {
        cn.upsertPermissionOverride(role)
          .deny(Permission.VIEW_CHANNEL)
          .queue(
              s -> logger.debug("Denied VIEW_CHANNEL on {}", cn.getName()),
              e -> logger.error("Failed to update permissions on {}: {}", cn.getName(), e.getMessage())
          );
    });

    channel.upsertPermissionOverride(role)
           .grant(Permission.VIEW_CHANNEL)
           .queue();

    channel.upsertPermissionOverride(ctx.getGuild().getPublicRole())
           .deny(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY)
           .queue();
}


    private void addDoorChannel(ComponentContext ctx, Role role) {
        ctx.getGuild().createTextChannel("🚪door").queue(
            channel -> {
                upsertPermissions(ctx, channel, role);
                addChannelToDatabase(ctx, channel.getId(), role.getId());
            }
        );
    }

    private void addRole(ComponentContext ctx) {
        ctx.getGuild().createRole().setName("Register").queue(
            role -> {
                addDoorChannel(ctx, role);
            }
        );
    }

    private void startDoorActions(ComponentContext ctx) {
        addRole(ctx);
    }

    @Override
    public void execute(ComponentContext ctx) {
        startDoorActions(ctx);
    }
}
