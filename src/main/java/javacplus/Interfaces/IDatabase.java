package javacplus.Interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabase {
    public String getName();

    public void open();

    public void close();

    public Connection getConnection() throws SQLException;
}
