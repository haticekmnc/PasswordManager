package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;

   public static Connection getConnection() throws SQLException {
    try {
        if (connection == null || connection.isClosed()) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db");
        }
    } catch (ClassNotFoundException | SQLException e) {
        System.err.println("Database connection error: " + e.getMessage());
        throw new SQLException("Failed to establish database connection.", e);
    }
    return connection;
}


    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
