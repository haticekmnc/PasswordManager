package com.company.secrest.vault.password;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import util.DBConnection;

@Named("passwordMB")
@SessionScoped
public class PasswordManager implements Serializable {

    private Passwords selectedPassword;
    private static final Logger LOGGER = Logger.getLogger(PasswordManager.class.getName());

    @PostConstruct
    public void init() {
        selectedPassword = new Passwords(); // selectedPassword nesnesini başlatma
        LOGGER.log(Level.INFO, "Parolaların veritabanından başlatılması ve yüklenmesi.");
        loadPasswordsFromDatabase();
    }

    public List<Passwords> loadPasswordsFromDatabase() {
        List<Passwords> passwords = new ArrayList<>();
        String sql = "SELECT * FROM passwords";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Passwords password = new Passwords();
                password.setId(resultSet.getLong("id"));
                password.setTitle(resultSet.getString("title"));
                password.setUrl(resultSet.getString("url"));
                password.setUsername(resultSet.getString("username"));
                password.setPassword(resultSet.getString("password"));
                password.setNotes(resultSet.getString("notes"));
                passwords.add(password);
            }
            LOGGER.log(Level.INFO, "Veritabanından {0} parola yüklendi.", passwords.size());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", passwords.size() + " passwords loaded."));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanından parolalar yüklenemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database Error", e.getMessage()));
        }
        return passwords;
    }

    public String savePassword() {
        if (selectedPassword == null || selectedPassword.getTitle().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Title cannot be empty."));
            return null;
        }

        String sql = "INSERT INTO passwords (title, url, username, password, notes) VALUES (?, ?, ?, ?, ?)";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedPassword.getTitle());
            statement.setString(2, selectedPassword.getUrl());
            statement.setString(3, selectedPassword.getUsername());
            statement.setString(4, AESUtil.encrypt(selectedPassword.getPassword()));
            statement.setString(5, selectedPassword.getNotes());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                LOGGER.log(Level.INFO, "Şifre veritabanına başarıyla kaydedildi.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password saved successfully."));
                loadPasswordsFromDatabase();
                return "index?faces-redirect=true";
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Parola veritabanına kaydedilemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return null;
    }

    public void deletePassword(Passwords password) {
        String sql = "DELETE FROM passwords WHERE id = ?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, password.getId());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                LOGGER.log(Level.INFO, "Parola veritabanından başarıyla silindi.");
                loadPasswordsFromDatabase();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanından parola silinemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void updatePassword(Passwords password) {
        String sql = "UPDATE passwords SET title=?, url=?, username=?, password=?, notes=? WHERE id=?";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password.getTitle());
            statement.setString(2, password.getUrl());
            statement.setString(3, password.getUsername());
            statement.setString(4, AESUtil.encrypt(password.getPassword()));
            statement.setString(5, password.getNotes());
            statement.setLong(6, password.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.log(Level.INFO, "Parola veritabanında başarıyla güncellendi.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password successfully updated."));
                loadPasswordsFromDatabase();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanındaki parola güncellenemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void resetForm() {
        this.selectedPassword = new Passwords();
        LOGGER.log(Level.INFO, "Form sıfırlandı.");
    }

    public Passwords getSelectedPassword() {
        return selectedPassword;
    }

    public void setSelectedPassword(Passwords selectedPassword) {
        this.selectedPassword = selectedPassword;
    }
}
