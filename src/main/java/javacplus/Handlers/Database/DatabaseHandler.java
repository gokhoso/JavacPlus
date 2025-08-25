package javacplus.Handlers.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javacplus.Interfaces.IDatabase;

public class DatabaseHandler {
    private final Logger logger = LoggerFactory.getLogger(DatabaseHandler.class);
    private final Map<String, IDatabase> databases = new ConcurrentHashMap<>();
    private final String basePackage = "javacplus.Databases";

    public DatabaseHandler() {
        logger.info("{} paketinden database'ler yükleniyor...", basePackage);

        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends IDatabase>> databaseClasses = reflections.getSubTypesOf(IDatabase.class);

        for (Class<? extends IDatabase> databaseClass : databaseClasses) {
            try {
                IDatabase database = databaseClass.getDeclaredConstructor().newInstance();
                if (databases.containsKey(database.getName())) {
                    logger.warn("Database zaten kayıtlı: {}", database.getName());
                    continue;
                }
                databases.put(database.getName(), database);
                logger.info("Database yüklendi: {}", database.getName());
            } catch (Exception e) {
                logger.error("Database yüklenirken hata oluştu: {}", databaseClass.getName(), e);
            }
        }

        databases.values().forEach(IDatabase::open);
    }

    public void stopDatabases() {
        databases.values().forEach(IDatabase::close);
    }

    public Connection getConnection(String name) throws SQLException {
        IDatabase database = databases.get(name);
        if (database == null) {
            throw new SQLException("Database bulunamadı: " + name);
        }
        return database.getConnection();
    }
}
