package log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import util.DBConnection;

@Named("logMB")
@SessionScoped
public class LogMB implements Serializable {

    private List<Logs> logEntries;
    private List<Logs> filteredLogEntries;
    private Date startDate;
    private Date endDate;
    private Date filterStartDate; // Ek filtreleme için başlangıç tarihi
    private Date filterEndDate;   // Ek filtreleme için bitiş tarihi
    // private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        logEntries = new ArrayList<>();
        filteredLogEntries = new ArrayList<>();
        loadLogs();
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
                Date parsedDate = dbDateFormat.parse(timestampStr);  // String'i Date'e çevir
                Timestamp timestamp = new Timestamp(parsedDate.getTime());  // Date'i Timestamp'e çevir
                log.setTimestamp(timestamp);

                logEntries.add(log);
            }
            filteredLogEntries = new ArrayList<>(logEntries);
        } catch (SQLException e) {
            System.err.println("Error loading all logs: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error parsing the timestamp: " + e.getMessage());
        }
    }

    public void filterByDate() {

        if (filterStartDate == null || filterEndDate == null) {
            filteredLogEntries = new ArrayList<>(logEntries);
            return;
        }
        filteredLogEntries.clear();

        String startDateTime = dbDateFormat.format(filterStartDate);
        String endDateTime = dbDateFormat.format(filterEndDate);

        String sql = "SELECT * FROM log WHERE timestamp >= ? AND timestamp <= ?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, startDateTime);
            statement.setString(2, endDateTime);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setId(rs.getLong("id"));
                log.setUsername(rs.getString("username"));
                log.setDescription(rs.getString("description"));

                String timestampStr = rs.getString("timestamp");
                Date parsedDate = null;
                try {
                    parsedDate = dbDateFormat.parse(timestampStr); // String'i Date'e çevir
                } catch (ParseException e) {
                    System.err.println("Error parsing the timestamp: " + e.getMessage());
                    continue; // Parse hatası durumunda bu log kaydını atla
                }
                Timestamp timestamp = new Timestamp(parsedDate.getTime()); // Date'i Timestamp'e çevir
                log.setTimestamp(timestamp);

                filteredLogEntries.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error filtering logs by date range: " + e.getMessage());
        }
    }

    public void addLogEntry(String username, String description, Long passwordId) {
        Logs log = new Logs();
        log.setUsername(username);
        log.setDescription(description);
        log.setTimestamp(new Date()); // Doğrudan tarih nesnesi oluşturuluyor
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

                String timestampStr = rs.getString("timestamp");
                Date parsedDate = null;
                try {
                    parsedDate = dbDateFormat.parse(timestampStr); // String'i Date'e çevir
                } catch (ParseException e) {
                    System.err.println("Error parsing the timestamp: " + e.getMessage());
                    continue; // Parse hatası durumunda bu log kaydını atla
                }
                Timestamp timestamp = new Timestamp(parsedDate.getTime()); // Date'i Timestamp'e çevir
                log.setTimestamp(timestamp);
                log.setPasswordId(rs.getLong("password_id"));
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

    public List<Logs> getFilteredLogEntries() {
        return filteredLogEntries;
    }

    public void setFilteredLogEntries(List<Logs> filteredLogEntries) {
        this.filteredLogEntries = filteredLogEntries;
    }

    public Date getFilterStartDate() {
        return filterStartDate;
    }

    public void setFilterStartDate(Date filterStartDate) {
        this.filterStartDate = filterStartDate;
    }

    public Date getFilterEndDate() {
        return filterEndDate;
    }

    public void setFilterEndDate(Date filterEndDate) {
        this.filterEndDate = filterEndDate;
    }

}
