package javacplus.Commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Handlers.Command.ContextCommand;
import javacplus.Interfaces.ICommand;

public class Register implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(Register.class);

    @Override
    public String[] getInformation() {
        return new String[] { "register", "Botu sunucuya kaydeder", "moderation", "15" };
    }

    private boolean isAlreadyRegistered(String serverID, String serverName, Connection connection) {
        String query = "SELECT COUNT(*) FROM SERVERS WHERE serverID = ?";
        logger.info("Checking if server is already registered: ID={}, Name={}", serverID, serverName);
        try {
            try (var pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, serverID); // FIX
                try (var rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        logger.info("Server registration check result: count={}", count);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void register(ContextCommand ctx, Connection connection) {
        String insert = "INSERT OR IGNORE INTO SERVERS(serverID, serverName) VALUES (?,?)";
        try {
            try (var pstmt = connection.prepareStatement(insert)) {
                pstmt.setString(1, ctx.getGuild().getId());
                pstmt.setString(2, ctx.getGuild().getName());
                pstmt.executeUpdate();
                ctx.sendMessage("Sunucu başarıyla kaydedildi!");
            }
        } catch (SQLException e) {
            ctx.sendMessage("Bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void execute(ContextCommand ctx) {
        if (ctx.getArgs().length > 2) {
            ctx.sendMessage("Çok fazla argüman!");
            return;
        }

        try (var connection = ctx.getSQLConnection("ServerDatabase")) {
            final boolean isRegistered = isAlreadyRegistered(
                    ctx.getServerId(), ctx.getGuild().getName(),
                    connection);

            logger.info("Server registration status: isRegistered={}", isRegistered);

            if (isRegistered) {
                if (ctx.getArgs().length == 2 && ctx.getArgs()[1].equals("-f")) {
                    register(ctx, connection);
                    return;
                }

                ctx.sendMessage("Bu sunucu zaten kayıtlı!\nOverwrite için -f parametresini kullan!");
                return;
            }

            register(ctx, connection);
        } catch (SQLException e) {
            ctx.sendMessage("Veritabanı hatası: " + e.getMessage());
            logger.error("Database error during registration", e);
        }
    }
}
