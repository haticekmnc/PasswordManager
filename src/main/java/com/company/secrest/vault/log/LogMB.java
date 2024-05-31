package com.company.secrest.vault.log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import com.company.secrest.vault.util.DBConnection;

@Named("logMB")
@SessionScoped
public class LogMB implements Serializable {

    private List<Logs> logEntries;
    private List<Logs> filteredLogEntries;
    private Date startDate;
    private Date endDate;

    @PostConstruct
    public void init() {
        logEntries = new ArrayList<>();
        filteredLogEntries = new ArrayList<>(logEntries);
        loadLogs();
    }

    public void filterLogs() {
        filteredLogEntries.clear();
        for (Logs log : logEntries) {
            if ((log.getTimestamp().after(startDate) || log.getTimestamp().equals(startDate))
                    && (log.getTimestamp().before(endDate) || log.getTimestamp().equals(endDate))) {
                filteredLogEntries.add(log);
            }
        }
    }
    
    public void resetDates() {
        startDate = null;
        endDate = null;
        filteredLogEntries = new ArrayList<>(logEntries); 
    }

    SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public void loadLogs() {
        logEntries.clear();
        String sql = "SELECT * FROM log";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setId(rs.getLong("id"));
                log.setUsername(rs.getString("username"));
                log.setDescription(rs.getString("description"));

                String timestampStr = rs.getString("timestamp");
                Date parsedDate = dbDateFormat.parse(timestampStr);
                log.setTimestamp(parsedDate);

                log.setPasswordId(rs.getLong("password_id"));
                log.setUserId(rs.getLong("user_id"));

                logEntries.add(log);
            }
            filteredLogEntries = new ArrayList<>(logEntries);
        } catch (SQLException | ParseException e) {
            System.err.println("Error loading all logs: " + e.getMessage());
        }
    }

    public void addLogEntryForPassword(String username, String description, Long passwordId) {
        Logs log = new Logs();
        log.setUsername(username);
        log.setDescription(description);
        log.setTimestamp(new Date());
        LogBean logBean = new LogBean();
        logBean.createLogEntry(log, passwordId, null);
        logEntries.add(log);
    }

    public void addLogEntryForUser(String username, String description, Long userId) {
        Logs log = new Logs();
        log.setUsername(username);
        log.setDescription(description);
        log.setTimestamp(new Date());
        LogBean logBean = new LogBean();
        logBean.createLogEntry(log, null, userId);
        logEntries.add(log);
    }

    public void loadLogsForPassword(Long passwordId) {
        logEntries.clear();
        String sql = "SELECT * FROM log WHERE password_id = ?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, passwordId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setId(rs.getLong("id"));
                log.setUsername(rs.getString("username"));
                log.setDescription(rs.getString("description"));

                String timestampStr = rs.getString("timestamp");
                Date parsedDate = dbDateFormat.parse(timestampStr);
                log.setTimestamp(parsedDate);

                log.setPasswordId(rs.getLong("password_id"));
                logEntries.add(log);
            }
        } catch (SQLException | ParseException e) {
            System.err.println("Error loading logs for password: " + e.getMessage());
        }
    }
    
    public void loadLogsForUser(Long userId) {
        logEntries.clear();
        String sql = "SELECT * FROM log WHERE user_id = ?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setId(rs.getLong("id"));
                log.setUsername(rs.getString("username"));
                log.setDescription(rs.getString("description"));

                String timestampStr = rs.getString("timestamp");
                Date parsedDate = dbDateFormat.parse(timestampStr);
                log.setTimestamp(parsedDate);

                log.setUserId(rs.getLong("user_id"));
                logEntries.add(log);
            }
        } catch (SQLException | ParseException e) {
            System.err.println("Error loading logs for user: " + e.getMessage());
        }
    }

    // Getters and Setters
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Logs> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(List<Logs> logEntries) {
        this.logEntries = logEntries;
    }

    public List<Logs> getFilteredLogEntries() {
        return filteredLogEntries;
    }

    public void setFilteredLogEntries(List<Logs> filteredLogEntries) {
        this.filteredLogEntries = filteredLogEntries;
    }

}
