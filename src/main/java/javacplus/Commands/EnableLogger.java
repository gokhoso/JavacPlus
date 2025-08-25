package javacplus.Commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Handlers.Command.ContextCommand;
import javacplus.Interfaces.ICommand;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

public class EnableLogger implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(EnableLogger.class);

    @Override
    public String[] getInformation() {
        return new String[] {
                "enablelogger",
                "Enables logger for server",
                "moderation",
                "15"
        };
    }

    private boolean isAlreadyEnabled(String serverID, String serverName, Connection connection) {
        String query = "SELECT logChannel FROM SERVERS WHERE serverID = ?";
        try {
            try (var pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, serverID); // FIX
                try (var rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String logChannel = rs.getString("logChannel");

                        return logChannel != null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void enableLogger(ContextCommand ctx, String logChannel, Connection connection) {
        String upsert = "INSERT INTO SERVERS (serverID, logChannel) " +
                "VALUES (?, ?) " +
                "ON CONFLICT(serverID) DO UPDATE SET logChannel = excluded.logChannel";
        try {
            try (var pstmt = connection.prepareStatement(upsert)) {
                pstmt.setString(1, ctx.getGuild().getId());
                pstmt.setString(2, logChannel);
                pstmt.executeUpdate();
                ctx.sendMessage("Log kanalı başarıyla kaydedildi!");
            }
        } catch (SQLException e) {
            ctx.sendMessage("Bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void execute(ContextCommand ctx) {
        if (ctx.getArgs().length > 3) {
            ctx.sendMessage("Çok fazla argüman!");
            return;
        }

        final var getMentions = ctx.getMessage().getMentions();
        final int channelSize = ctx.getMessage().getMentions().getChannels().size();
        GuildChannel logChannel = channelSize == 1 ? getMentions.getChannels().get(0) : null;

        if (logChannel == null || !(logChannel instanceof TextChannel)) {
            ctx.sendMessage("Lütfen sadece bir text kanalı etiketleyin!");
            return;
        }

        logChannel = (TextChannel) logChannel;

        try (var connection = ctx.getSQLConnection("ServerDatabase")) {
            final boolean isEnabled = isAlreadyEnabled(
                    ctx.getServerId(), ctx.getGuild().getName(),
                    connection);

            if (isEnabled) {
                if (ctx.getArgs().length == 3 && ctx.getArgs()[2].equals("-f")) {
                    enableLogger(ctx, logChannel.getId(), connection);
                    return;
                }

                ctx.sendMessage("Bu sunucu zaten kayıtlı!\nOverwrite için -f parametresini kullan!");
                return;
            }

            enableLogger(ctx, logChannel.getId(), connection);
        } catch (SQLException e) {
            ctx.sendMessage("Veritabanı hatası: " + e.getMessage());
            logger.error("Database error during enabling logger", e);
        }
    }
}
