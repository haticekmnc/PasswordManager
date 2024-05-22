package com.company.secrest.vault.password;

import com.company.secrest.vault.entity.AuditInfo;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import com.company.secrest.vault.log.LogMB;

import com.company.secrest.vault.util.DBConnection;

@Named("passwordMB")
@SessionScoped
public class PasswordManager implements Serializable {

    private Passwords selectedPassword;
    private static final Logger LOGGER = Logger.getLogger(PasswordManager.class.getName());

    @PostConstruct
    public void init() {
        selectedPassword = new Passwords(); // selectedPassword nesnesini başlatma

        loadPasswordsFromDatabase();
    }

    @Inject
    private UserSession userSession;

    @Inject
    private LogMB logMB;

    public List<Passwords> loadPasswordsFromDatabase() {

        List<Passwords> passwords = new ArrayList<>();
        String sql = "SELECT * FROM passwords";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Passwords password = new Passwords();
                password.setId(resultSet.getLong("id"));
                password.setSystemInformation(resultSet.getString("SystemInformation"));
                password.setAccessInformation(resultSet.getString("AccessInformation"));
                password.setUsername(resultSet.getString("Username"));
                password.setPassword(resultSet.getString("Password"));
                password.setNotes(resultSet.getString("Notes"));
                passwords.add(password);
            }

            // Parola yükleme işlemi başarılı olduğunda log girişi ekle
            // Veri yükleme işlemi...
            //logMB.addLogEntry(userSession.getUsername(), "Veritabanından şifreler yüklendi.", null);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanından parolalar yüklenemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database Error", e.getMessage()));
            // Parola yükleme işlemi sırasında hata oluştuğunda log girişi ekle
            //logMB.addLog(userSession.getUsername(), "Sisteme veritabanından parolalar yüklenirken hata oluştu: " + e.getMessage(), Long.MAX_VALUE);

        }
        return passwords;
    }

    public String savePassword() {
        if (selectedPassword == null || selectedPassword.getSystemInformation().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Başlık boş olamaz."));
            return null;
        }

        // Null kontrolü yaparak AES şifrelemeyi güvence altına al
        String encryptedPassword = null;
        if (selectedPassword.getPassword() != null) {
            encryptedPassword = AESUtil.encrypt(selectedPassword.getPassword());
        }

        // Audit bilgilerini oluştur
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setAddUser(userSession.getUsername());
        auditInfo.setAddDate(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(auditInfo.getAddDate());
        auditInfo.setAddDate(auditInfo.getAddDate());

        selectedPassword.setAuditInfo(auditInfo);

        String sql = "INSERT INTO passwords (systemInformation, accessInformation, username, password, notes, addUser, addDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedPassword.getSystemInformation());
            statement.setString(2, selectedPassword.getAccessInformation());
            statement.setString(3, selectedPassword.getUsername());
            statement.setString(4, encryptedPassword); // Şifrelenmiş parolayı kullan
            statement.setString(5, selectedPassword.getNotes());
            statement.setString(6, selectedPassword.getAuditInfo().getAddUser());
            statement.setString(7, formattedDate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {

                LOGGER.log(Level.INFO, "Şifre veritabanına başarıyla kaydedildi.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password saved successfully."));
                // Yeni şifre kaydedildiğinde log girişi ekleyin
                if (selectedPassword != null) {
                    logMB.addLogEntry(userSession.getUsername(), "İçin yeni şifre eklendi: " + selectedPassword.getSystemInformation(), selectedPassword.getId());
                }

                loadPasswordsFromDatabase();
                return "index.xhtml?faces-redirect=true";
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Parola veritabanına kaydedilemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            //logMB.addLog("Sisteme", "şifre kaydedilirken hata oluştu: " + e.getMessage(),Long.MAX_VALUE);
        }
        return null;
    }
    

public void deletePassword(Passwords password) {
    if (password == null) {
        LOGGER.severe("Attempt to delete a null password object");
        return;
    }
    LOGGER.info("Deleting password with ID: " + password.getId());
    String sql = "DELETE FROM passwords WHERE id = ?";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setLong(1, password.getId());
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            LOGGER.info("Successfully deleted password with ID: " + password.getId());
            loadPasswordsFromDatabase(); // Silme işlemi sonrası veritabanını yeniden yükle
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Failed to delete password.", e);
    }
}






    public void updatePassword(Passwords password) {
        LOGGER.log(Level.INFO, "updatePassword methodu çağırıldı.");
        if (password == null) {
            LOGGER.log(Level.WARNING, "Güncelleme için şifre verilmedi.");
            return;
        }

        if (selectedPassword != null) {
            LOGGER.log(Level.INFO, "Şifre {0} güncelleniyor.", selectedPassword.getSystemInformation());
        }

        AuditInfo auditInfo = password.getAuditInfo();
        if (auditInfo == null) {
            auditInfo = new AuditInfo();
            password.setAuditInfo(auditInfo);
            LOGGER.log(Level.INFO, "Mevcut denetim bilgisi bulunamadı. Yeni AuditInfo oluşturuldu.");
        }

        // Kullanıcı adı ve tarih bilgilerini güncelle
        auditInfo.setUpdUser(userSession.getUsername());
        auditInfo.setUpdDate(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(auditInfo.getUpdDate()); // Tarihi "dd.MM.yyyy" formatına dönüştür
        auditInfo.setUpdDate(auditInfo.getUpdDate());
        LOGGER.log(Level.INFO, "Güncelleme kullanıcısı {0} olarak ayarlandı ve güncelleme tarihi {1}", new Object[]{auditInfo.getUpdUser(), formattedDate});

        String sql = "UPDATE passwords SET systemInformation=?, accessInformation=?, username=?, password=?, notes=?, updUser=?, updDate=? WHERE id=?";

        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password.getSystemInformation());
            statement.setString(2, password.getAccessInformation());
            statement.setString(3, password.getUsername());
            statement.setString(4, AESUtil.encrypt(password.getPassword()));
            statement.setString(5, password.getNotes());
            statement.setString(6, auditInfo.getUpdUser());
            statement.setString(7, formattedDate); // Tarihi string olarak kaydet
            statement.setLong(8, password.getId());

            int rowsUpdated = statement.executeUpdate();
            LOGGER.log(Level.INFO, "Parola güncellenmeye çalışıldı, SQL güncelleme sayısı: {0}", rowsUpdated);

            if (rowsUpdated > 0) {
                LOGGER.log(Level.INFO, "Parola veritabanında başarıyla güncellendi.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "BAŞARILI!", "Password BAŞARIYLA GÜNCELLENDİ."));
                loadPasswordsFromDatabase(); // DataTable' ı güncelle

                // Güncellenen şifre için log girişi ekle
                if (password != null) {
                    logMB.addLogEntry(userSession.getUsername(), "Şifre güncellendi: " + password.getSystemInformation(), password.getId());
                }

            } else {
                LOGGER.log(Level.WARNING, "veritabanında güncellenen satır yok!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanındaki parola güncellenemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Kayıt güncellenemedi: " + e.getMessage()));
            //logMB.addLog("Sistemden", "kayıt güncellenemedi: " + e.getMessage(),Long.MAX_VALUE);
        }
    }
    // edit butonu için selectedPassword

    public void prepareUpdate(Passwords password) {
        this.selectedPassword = password;

    }

    public void resetForm() {
        selectedPassword = new Passwords();
    }
    
    public boolean isUrl(String value) {
    return value != null && (value.startsWith("http://") || value.startsWith("https://"));
}


    public Passwords getSelectedPassword() {
        return selectedPassword;
    }

    public void setSelectedPassword(Passwords selectedPassword) {
        this.selectedPassword = selectedPassword;
    }
}