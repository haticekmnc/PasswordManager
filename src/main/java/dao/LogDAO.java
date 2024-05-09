/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author hatice.kemence
 */
import java.sql.*;

public class LogDAO {
    // Veritabanı bağlantısı için gerekli bilgiler
    private static final String URL = "jdbc:sqlite:C:\\Users\\Hatice Kemençe\\Desktop\\mydatabase\\haticeDatabase.db";
   
    public void saveLog(String username, String description) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            // Veritabanı bağlantısı oluşturuldu
            String query = "INSERT INTO log (username, description, timestamp) VALUES (?, ?, datetime('now'))";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, description);
                // Sorguyu çalıştır
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayLogs() {
        try (Connection connection = DriverManager.getConnection(URL)) {
            // Veritabanı bağlantısı oluşturuldu
            String query = "SELECT * FROM log";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                // ResultSet'teki her kaydı işle
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String description = resultSet.getString("description");
                    String timestamp = resultSet.getString("timestamp");
                    System.out.println("ID: " + id + ", Username: " + username + ", Description: " + description + ", Timestamp: " + timestamp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

