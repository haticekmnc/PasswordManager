
package com.company.secrets.vault.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.company.secrest.vault.util.DBConnection;

public class UserDAO {

    public boolean registerUser(String username, String password, String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO users (username, password, email, isAdmin) VALUES (?, ?, ?, ?)")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setBoolean(4, false);
           

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
