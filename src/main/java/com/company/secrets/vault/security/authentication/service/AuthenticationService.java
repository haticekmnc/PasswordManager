/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrets.vault.security.authentication.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 *
 * @author hatice.kemence
 */
@Named
@Stateless
public class AuthenticationService {
    private static final String URL = "jdbc:sqlite:C:\\Users\\Hatice Kemençe\\Desktop\\mydatabase\\haticeDatabase.db";

    public boolean authenticate(String username, String password) {
        String dbUsername = "";
        String dbPassword = "";
        
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT username, password FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        dbUsername = resultSet.getString("username");
                        dbPassword = resultSet.getString("password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return username.equals(dbUsername) && password.equals(dbPassword);
    }
    
}
