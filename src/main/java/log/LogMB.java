/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package log;

/**
 *
 * @author hatice.kemence
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import util.DBConnection;

@Named("logMB")
@SessionScoped
public class LogMB implements Serializable {

    private List<Logs> logEntries;

    @PostConstruct
    public void init() {
        logEntries = new ArrayList<>();
        String sql = "SELECT * FROM log";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Logs log = new Logs();
                log.setId(rs.getLong("id"));
                log.setUsername(rs.getString("username"));
                log.setDescription(rs.getString("description"));
                log.setTimestamp(rs.getString("timestamp"));
                logEntries.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error loading all logs: " + e.getMessage());
        }
    }

    public void addLogEntry(String username, String description, Long passwordId) {
        Logs log = new Logs();
        log.setUsername(username);
        log.setDescription(description);
        log.setTimestamp(new Date().toString());
        LogBean logBean = new LogBean();
        logBean.createLogEntry(log, passwordId);
        logEntries.add(log);
    }

    public void loadLogsForPassword(Long passwordId) {
        logEntries.clear(); // Önceki log kayıtlarını temizle
        String sql = "SELECT * FROM log WHERE password_id = ?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, passwordId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setId(rs.getLong("id"));
                log.setUsername(rs.getString("username"));
                log.setDescription(rs.getString("description"));
                log.setTimestamp(rs.getString("timestamp"));
                log.setPasswordId(rs.getLong("password_id")); // password_id'yi de log nesnesine ekle
                logEntries.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error loading logs for password: " + e.getMessage());
        }
    }

    // Getter ve setter metotları
    public List<Logs> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(List<Logs> logEntries) {
        this.logEntries = logEntries;
    }

}
