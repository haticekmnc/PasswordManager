package com.company.secrest.vault.password;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
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

@Named("passwordMB")
@SessionScoped
public class PasswordManager implements Serializable {

    private List<Passwords> passwords = new ArrayList<>();
    private Passwords selectedPassword; //form alanlarını tutar
    private final String dbUrl = "jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db";

    private static final Logger LOGGER = Logger.getLogger(PasswordManager.class.getName());

    @PostConstruct
    public void init() {
        selectedPassword = new Passwords(); // selectedPassword'ü yeni bir Passwords nesnesiyle başlat
        loadPasswordsFromDatabase(); // Veritabanından şifreleri yükle
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    public void loadPasswordsFromDatabase() {
        passwords.clear();
        String sql = "SELECT * FROM passwords";
        try ( Connection connection = getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Passwords password = new Passwords();
                password.setId(resultSet.getLong("id"));
                password.setTitle(resultSet.getString("title"));
                password.setUrl(resultSet.getString("url"));
                password.setUsername(resultSet.getString("username"));
                password.setPassword(resultSet.getString("password")); // Düzeltildi
                password.setNotes(resultSet.getString("notes"));
                passwords.add(password);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", passwords.size() + " passwords loaded."));
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database Error", e.getMessage()));
        }
    }

    public String savePassword() {
        
    if (selectedPassword == null || selectedPassword.getTitle() == null || selectedPassword.getTitle().trim().isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Title cannot be empty."));
        return null; // İşlemi durdur ve hata mesajı göster
    }

    String sql = "INSERT INTO passwords (title, url, username, password, notes) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        
        statement.setString(1, selectedPassword.getTitle());
        statement.setString(2, selectedPassword.getUrl());
        statement.setString(3, selectedPassword.getUsername());
        statement.setString(4, selectedPassword.getPassword());
        statement.setString(5, selectedPassword.getNotes());

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            LOGGER.log(Level.INFO, "Şifre başarıyla veritabanına kaydedildi.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password saved successfully."));
            // Veritabanından şifreleri yeniden yükle, böylece kaydedilen şifre listeye dahil edilir
            loadPasswordsFromDatabase();
            return "index?faces-redirect=true"; // Başarılı kayıttan sonra yönlendirme yap
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Veritabanı kaydetme işlemi sırasında bir hata meydana geldi.", e);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
    }
    return null; // Bir hata oluşursa, yönlendirme yapma
}


    public void deletePassword(Passwords password) {
        String sql = "DELETE FROM passwords WHERE id = ?";
        try ( Connection connection = getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, password.getId());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                loadPasswordsFromDatabase(); // Veritabanını yeniden yükle

            }
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

        }
    }

    public void updatePassword(Passwords password) {
        String sql = "UPDATE passwords SET title=?, url=?, username=?, password=?, notes=? WHERE id=?";
        try ( Connection connection = getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password.getTitle());
            statement.setString(2, password.getUrl());
            statement.setString(3, password.getUsername());
            statement.setString(4, password.getPassword());
            statement.setString(5, password.getNotes());
            statement.setLong(6, password.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password successfully updated."));
            }
            loadPasswordsFromDatabase();
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
    public void resetForm() {
    this.selectedPassword = new Passwords();
}


    // Getter ve Setter metodları
    public List<Passwords> getPasswords() {
        return passwords;
    }

    public Passwords getSelectedPassword() {
        return selectedPassword;
    }

    public void setSelectedPassword(Passwords selectedPassword) {
        this.selectedPassword = selectedPassword;
    }
}
