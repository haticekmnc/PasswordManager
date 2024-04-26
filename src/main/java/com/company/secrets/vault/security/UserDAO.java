package com.company.secrets.vault.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    
    private static final String URL = "jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db";

    public boolean registerUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();
                return true; // İşlem başarılı olduğunda true döndür
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hata durumunda false döndür
        }
    }
}
