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
import java.util.List;

import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@ManagedBean
@RequestScoped
@ViewScoped
@Named("logMB")
@SessionScoped
public class LogMB implements Serializable {

    private List<Logs> logEntries;

    @PostConstruct
    public void init() {
        logEntries = new ArrayList<>();
    }

    public void addLogEntry(String username, String description) {
        Logs log = new Logs();
        log.setUsername(username);
        log.setDescription(description);
        log.setTimestamp(new Date().toString()); // Burada istediğiniz zaman damgası biçimini ayarlayabilirsiniz
        LogBean logBean = new LogBean();
        logBean.createLogEntry(log);
        logEntries.add(log);
    }

    // Getter ve setter metotları
    public List<Logs> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(List<Logs> logEntries) {
        this.logEntries = logEntries;
    }

}
