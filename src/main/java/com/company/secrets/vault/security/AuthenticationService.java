package com.company.secrets.vault.security;

import com.company.secrest.vault.password.AESUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
public class AuthenticationService {
    private static final String URL = "jdbc:sqlite:C:\\Users\\Hatice Kemençe\\Desktop\\mydatabase\\haticeDatabase.db";

    public boolean authenticate(String username, String password) {
        String dbPassword = "";
        
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        dbPassword = resultSet.getString("password");
                    } else {
                        System.out.println("Kullanıcı bulunamadı: " + username);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Kullanıcının girdiği şifreyi AESUtil ile hashleyip veritabanındaki hash ile karşılaştırıyoruz
        String encryptedPassword = AESUtil.encrypt(password);
        return encryptedPassword.equals(dbPassword);
    }
    
    public boolean checkAdmin(String username) {
        boolean isAdmin = false;
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT isAdmin FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        isAdmin = resultSet.getBoolean("isAdmin");
                    } else {
                        System.out.println("Kullanıcı bulunamadı: " + username);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAdmin;
    }
}
