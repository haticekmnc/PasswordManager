package com.company.secrest.vault.user;

import com.company.secrest.vault.entity.AuditInfo;
import com.company.secrest.vault.log.LogMB;
import com.company.secrest.vault.password.AESUtil;
import com.company.secrest.vault.password.UserSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import com.company.secrest.vault.util.DBConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named("userMB")
@SessionScoped
public class UserMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UserMB.class.getName());

    private List<User> users;
    private User selectedUser;

    @PostConstruct
    public void init() {
        selectedUser = new User();
        users = new ArrayList<>();
        loadUsers();
    }

    @Inject
    private UserSession userSession;
    
    @Inject
    private LogMB logMB;
    
    

    public void loadUsers() {
        users.clear();
        String sql = "SELECT * FROM users";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setIsAdmin(rs.getBoolean("isAdmin"));
                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading users", e);
        }
    }

    public void deleteUser(User user) {
        if (user == null) {
            LOGGER.severe("Attempt to delete a null user object");
            return;
        }
        LOGGER.info("Deleting user with ID: " + user.getId());
        String sql = "DELETE FROM users WHERE id = ?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                LOGGER.info("Successfully deleted user with ID: " + user.getId());
                
                // Log message
                String logMessage = String.format("Kullanıcı %s, kullanıcı %s'yi sildi", userSession.getUsername(), user.getUsername());
                logMB.addLogEntryForUser(userSession.getUsername(), logMessage, user.getId());
                
               
                loadUsers();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete user.", e);
        }
    }

   public String saveUser() {
    // Kullanıcı şifresini şifrele
    String encryptedPassword = AESUtil.encrypt(selectedUser.getPassword());
    if (encryptedPassword == null) {
        LOGGER.severe("Password encryption failed.");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Encryption Error", "Failed to encrypt password."));
        return null;
    }

    // Audit bilgilerini ayarla
    AuditInfo auditInfo = new AuditInfo();
    auditInfo.setAddUser(userSession.getUsername()); // Şu anki kullanıcı adını ayarla
    auditInfo.setAddDate(new Date()); // Şu anki tarihi ayarla

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    String formattedDate = dateFormat.format(auditInfo.getAddDate());

    // SQL sorgusu
    String sql = "INSERT INTO users (username, password, email, isAdmin, addUser, addDate) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, selectedUser.getUsername());
        statement.setString(2, encryptedPassword); // Şifrelenmiş şifre veritabanına kaydediliyor
        statement.setString(3, selectedUser.getEmail());
        statement.setBoolean(4, selectedUser.isIsAdmin());
        statement.setString(5, auditInfo.getAddUser());
        statement.setString(6, formattedDate);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            LOGGER.info("User successfully added to the database with encrypted password.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User successfully added with encrypted password."));
            // Log message
                String logMessage = String.format("Kullanıcı %s, kullanıcı %s'yi ekledi", userSession.getUsername(), selectedUser.getUsername());
                logMB.addLogEntryForUser(userSession.getUsername(), logMessage, selectedUser.getId());

                loadUsers();
           
            return "index.xhtml?faces-redirect=true";
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Failed to add user to database.", e);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add user: " + e.getMessage()));
    }
    return null;
}




    public void updateUser(User user) {

        if (selectedUser == null) {
            LOGGER.severe("Selected user is null.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No user selected."));

        }

        if (selectedUser.getAuditInfo() == null) {
            LOGGER.severe("Audit info is null.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Audit info is missing."));

        }
        String encryptedPassword = AESUtil.encrypt(user.getPassword());
        AuditInfo auditInfo = user.getAuditInfo();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedUpdDate = dateFormat.format(auditInfo.getUpdDate());

        String sql = "UPDATE users SET username=?, password=?, email=?, isAdmin=?, updUser=?, updDate=? WHERE id=?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, encryptedPassword);
            statement.setString(3, user.getEmail());
            statement.setBoolean(4, user.isIsAdmin());
            statement.setString(5, auditInfo.getUpdUser());
            statement.setString(6, formattedUpdDate);
            statement.setLong(7, user.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.info("User successfully updated.");
                if (user != null) {
                    logMB.addLogEntryForUser(userSession.getUsername(), "User güncellendi: " + user.getUsername(), user.getId());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update user.", e);
        }
    }

    public void prepareUpdate(User user) {
        this.selectedUser = user;
        if (this.selectedUser != null && this.selectedUser.getAuditInfo() == null) {
            this.selectedUser.setAuditInfo(new AuditInfo());
            // Şu anki kullanıcı adını ve tarihi set edin
            this.selectedUser.getAuditInfo().setUpdUser(userSession.getUsername());
            this.selectedUser.getAuditInfo().setUpdDate(new Date());
            
             // Log message
            String logMessage = String.format("Kullanıcı %s, kullanıcı %s'yi güncelledi.", userSession.getUsername(), this.selectedUser.getUsername());
            logMB.addLogEntryForUser(userSession.getUsername(), logMessage, this.selectedUser.getId());
        }
        
    }

    //user eknen formu temizle
    public void resetAddUserForm() {
        selectedUser = new User();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}
