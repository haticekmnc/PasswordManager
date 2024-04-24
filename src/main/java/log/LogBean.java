package log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import util.DBConnection;

@Named("logBean")
@ViewScoped
public class LogBean implements Serializable {

    public void createLogEntry(Logs log) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement("INSERT INTO log (username, description, timestamp) VALUES (?, ?, ?)");
            statement.setString(1, log.getUsername());
            statement.setString(2, log.getDescription());
            statement.setString(3, log.getTimestamp());
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
}
