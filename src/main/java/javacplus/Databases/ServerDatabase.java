package javacplus.Databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javacplus.Interfaces.IDatabase;

public class ServerDatabase implements IDatabase {
        private static final String DB_URL = "jdbc:sqlite:src/main/java/javacplus/Databases/RawData/ServerData.db";
        private final Logger logger = LoggerFactory.getLogger(ServerDatabase.class);
        private HikariDataSource dataSource;

        @Override
        public String getName() {
                return "ServerDatabase";
        }

        private void openTables() {
                logger.info("Server Database tabloları kontrol ediliyor...");
                try (Connection connection = getConnection();
                                Statement stmt = connection.createStatement()) {

                        String sql = """
                                        CREATE TABLE IF NOT EXISTS SERVERS (
                                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                                            logChannel TEXT,
                                            serverID TEXT NOT NULL UNIQUE,
                                            serverName TEXT,
                                            joinLeaveChannel TEXT,
                                            doorChannel TEXT,
                                            registerRole TEXT,
                                            memberRole TEXT
                                        );
                                        """;
                        stmt.execute(sql);
                        logger.info("Server Database tabloları hazır!");
                } catch (SQLException e) {
                        logger.error("Veritabanı işlemi sırasında hata oluştu", e);
                }
        }

        @Override
        public void open() {
                try {
                        HikariConfig config = new HikariConfig();
                        config.setJdbcUrl(DB_URL);
                        config.setMaximumPoolSize(1); // SQLite için tek connection en güvenli
                        config.setPoolName("ServerDatabasePool");
                        dataSource = new HikariDataSource(config);

                        logger.info("SQLite başlatıldı!");
                        openTables();
                } catch (Exception e) {
                        logger.error("Database açılırken hata oluştu", e);
                }
        }

        @Override
        public void close() {
                if (dataSource != null && !dataSource.isClosed()) {
                        dataSource.close();
                        logger.info("Database pool kapatıldı.");
                }
        }

        @Override
        public Connection getConnection() throws SQLException {
                if (dataSource == null) {
                        throw new SQLException("Database açılmamış!");
                }
                return dataSource.getConnection();
        }
}
