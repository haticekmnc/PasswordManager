package com.company.secrest.vault.log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.company.secrest.vault.util.DBConnection;

@Named("logBean")
@ViewScoped
public class LogBean implements Serializable {

    public void createLogEntry(Logs log, Long passwordId, Long userId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnection.getConnection();
            String sql = "INSERT INTO log (username, description, timestamp, password_id, user_id) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, log.getUsername());
            statement.setString(2, log.getDescription());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            statement.setString(3, dateFormat.format(log.getTimestamp()));

            if (passwordId == null) {
                statement.setNull(4, java.sql.Types.BIGINT);
            } else {
                statement.setLong(4, passwordId);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating log entry: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    
    public void createLogEntryUser(Logs log, Long userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DBConnection.getConnection();
            String sql = "INSERT INTO log (username, description, timestamp, user_id) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, log.getUsername());
            statement.setString(2, log.getDescription());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            statement.setString(3, dateFormat.format(log.getTimestamp()));

            if (userId == null) {
                statement.setNull(4, java.sql.Types.BIGINT);
            } else {
                statement.setLong(4, userId);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating log entry: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        
        
    }
    }}
