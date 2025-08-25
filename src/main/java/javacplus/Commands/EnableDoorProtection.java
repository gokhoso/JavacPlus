package javacplus.Commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Handlers.Command.ContextCommand;
import javacplus.Interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

public class EnableDoorProtection implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(EnableLogger.class);

    @Override
    public String[] getInformation() {
        return new String[] {
                "enabledoor",
                "Enables door protection server",
                "moderation",
                "15"
        };
    }

    private boolean isAlreadyEnabled(ContextCommand ctx) {
        String serverID = ctx.getServerId();

        String query = "SELECT doorChannel, memberRole FROM SERVERS WHERE serverID = ?";
        try (Connection connection = ctx.getSQLConnection("ServerDatabase")) {
            logger.info("Checking if registered");
            try (var pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, serverID);
                try (var rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String doorChannel = rs.getString("doorChannel");
                        String memberRole = rs.getString("memberRole");
                        logger.info("doorChannel=" + doorChannel + ", memberRole=" + memberRole);

                        boolean hasDoorChannel = (doorChannel != null && !doorChannel.isBlank());
                        boolean hasMemberRole = (memberRole != null && !memberRole.isBlank());

                        return hasDoorChannel && hasMemberRole;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Returning null");
        return false;
    }

    private void createChannel(ContextCommand ctx, String memberRole) {
        ctx.getGuild().createTextChannel("🚪door")
                .setTopic("Giriş koruma kanalı")
                .queue(channel -> {
                    Role role = ctx.getGuild().getRoleById(memberRole);
                    if (role == null) {
                        ctx.sendMessage("Geçersiz rol ID'si!");
                        return;
                    }

                    channel.upsertPermissionOverride(ctx.getGuild().getPublicRole())
                            .deny(Permission.MESSAGE_HISTORY, Permission.VIEW_CHANNEL)
                            .queue();

                    channel.upsertPermissionOverride(role)
                            .grant(Permission.VIEW_CHANNEL)
                            .queue();

                    enableDoor(ctx, channel.getId(), memberRole);
                }, error -> {
                    ctx.sendMessage("Kanal oluşturulamadı: " + error.getMessage());
                    logger.error("Kanal oluşturulamadı", error);
                });
    }

    private void enableDoor(ContextCommand ctx, String doorChannel, String memberRole) {
        String upsert = "INSERT INTO SERVERS (serverID, doorChannel, memberRole) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT(serverID) DO UPDATE SET doorChannel = excluded.doorChannel, memberRole = excluded.memberRole";
        try (Connection connection = ctx.getSQLConnection("ServerDatabase")) {
            try (var pstmt = connection.prepareStatement(upsert)) {
                pstmt.setString(1, ctx.getGuild().getId());
                pstmt.setString(2, doorChannel);
                pstmt.setString(3, memberRole);
                pstmt.executeUpdate();
                ctx.sendMessage("Log kanalı başarıyla kaydedildi!");
            } catch (SQLException e) {
                ctx.sendMessage("Bir hata oluştu: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        } catch (SQLException e) {
            ctx.sendMessage("Veritabanı hatası: " + e.getMessage());
            logger.error("Database error during enabling door protection", e);
        }
    }

    @Override
    public void execute(ContextCommand ctx) {
        if (ctx.getArgs().length > 3) {
            ctx.sendMessage("Çok fazla argüman!");
            return;
        }

        var mentionRoles = ctx.getMessage().getMentions().getRoles();
        if (mentionRoles.size() == 0) {
            ctx.sendMessage("Bir rol etiketlemelisin!");
            return;
        }

        var role = mentionRoles.get(0);
        try {
            final boolean isEnabled = isAlreadyEnabled(ctx);

            if (isEnabled) {
                if (ctx.getArgs().length == 3 && ctx.getArgs()[2].equals("-f")) {
                    System.err.println("REGISTER AGAIN");
                    createChannel(ctx, role.getId());
                    return;
                }

                ctx.sendMessage("Bu sunucu zaten kayıtlı!\nOverwrite için -f parametresini kullan!");
                return;
            }

            createChannel(ctx, role.getId());
        } catch (Exception e) {
            ctx.sendMessage("Veritabanı hatası: " + e.getMessage());
            logger.error("Database error during enabling logger", e);
        }
    }
}
