package javacplus.Commands;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Handlers.Command.ContextCommand;
import javacplus.Handlers.Component.ComponentMain;
import javacplus.Interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class SecuritySetup implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(SecuritySetup.class);

    private final String doorInsertQuery = """
            INSERT INTO SERVERS (serverID, doorChannel, memberRole)
            VALUES (?, ?, ?)
            ON CONFLICT(serverID) DO UPDATE SET doorChannel = excluded.doorChannel, memberRole = excluded.memberRole
            """;

    @Override
    public String[] getInformation() {
        return new String[] {
                "security",
                "Enables security for this server",
                "moderation",
                "15"
        };
    }

    private void addDoorToDatabase(ContextCommand ctx, String channelID, String roleID) {
        try (var connection = ctx.getSQLConnection("ServerDatabase")) {
            try (var pstmt = connection.prepareStatement(doorInsertQuery)) {
                pstmt.setString(1, ctx.getServerId());
                pstmt.setString(2, channelID);
                pstmt.setString(3, roleID);
                pstmt.executeUpdate();
                logger.debug("Inserted/Updated doorChannel and memberRole for serverID {}", ctx.getServerId());
            }
        } catch (SQLException e) {
            logger.error("SQLEXCEPTION: {}", e);
        }
    }

    private void upsertPermissions(ContextCommand ctx, TextChannel channel, Role role) {
        channel.upsertPermissionOverride(role).grant(Permission.VIEW_CHANNEL).queue();
        channel.upsertPermissionOverride(ctx.getGuild().getPublicRole())
                .deny(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY).queue();
    }

    private void addDoorProtection(ContextCommand ctx, Role role) {
        ctx.getGuild().createTextChannel("🚪door").queue(
                channel -> {
                    upsertPermissions(ctx, channel, role);
                    addDoorToDatabase(ctx, channel.getId(), role.getId());
                });
    }

    @Override
    public void execute(ContextCommand ctx) {
        ComponentMain componentMain = ctx.getComponentHandler();
        ctx.getChannel().sendMessage("Security test").queue(
                message -> {
                    // componentHandler.createButtonPrimary(message, ctx.getAuthorId(),
                    // "securitybutton", "Test Label");

                    String[] information = new String[] {
                        "securitybutton",
                        message.getId()
                    };

                    logger.debug("Adding SecurityComponentId: " + message.getId());
                    componentMain.getButtonManager().getCreator().addUserButton(ctx.getAuthorId(), message, information, ButtonStyle.SECONDARY);
                    //componentHandler.getCreator().createButtonPrimary(message, ctx.getAuthorId(), information , "Accept");
                });
    }

}
