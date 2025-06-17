package lk.ijse.project_01.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;

    private final String URL = "jdbc:mysql://localhost:3306/villa";
    private final String USER = "root";
    private final String PASSWORD = "vihan@46";

    private DBConnection() {

    }

    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Deprecated
    public Connection getConnection() throws SQLException {
        return getNewConnection();
    }
}
