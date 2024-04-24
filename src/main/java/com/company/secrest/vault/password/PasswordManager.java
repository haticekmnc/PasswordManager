package com.company.secrest.vault.password;

import entity.AuditInfo;
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
import log.LogMB;
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
                password.setTitle(resultSet.getString("title"));
                password.setUrl(resultSet.getString("url"));
                password.setUsername(resultSet.getString("username"));
                password.setPassword(resultSet.getString("password"));
                password.setNotes(resultSet.getString("notes"));
                passwords.add(password);
            }
            //LOGGER.log(Level.INFO, "Veritabanından {0} parola yüklendi.", passwords.size());
           // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", passwords.size() + " passwords loaded."));
            // Parola yükleme işlemi başarılı olduğunda log girişi ekle
            logMB.addLogEntry(userSession.getUsername(),  " Sisteme veritabanından yüklenen toplam veri sayısı: " +passwords.size() );
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanından parolalar yüklenemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database Error", e.getMessage()));
            // Parola yükleme işlemi sırasında hata oluştuğunda log girişi ekle
            logMB.addLogEntry(userSession.getUsername(),"Sisteme veritabanından parolalar yüklenirken hata oluştu: " + e.getMessage());
        }
        return passwords;
    }

    public String savePassword() {
        if (selectedPassword == null || selectedPassword.getTitle().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Başlık boş olamaz."));
            return null;
        }

        // Audit bilgilerini oluştur
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setAddUser(userSession.getUsername()); // UserSession'dan kullanıcı adını al
        auditInfo.setAddDate(new Date()); // Şu anki tarih
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(auditInfo.getAddDate()); // Tarihi "dd.MM.yyyy" formatına dönüştür
        auditInfo.setAddDate(auditInfo.getAddDate());

        // Password nesnesine AuditInfo'yu set et
        selectedPassword.setAuditInfo(auditInfo);

        String sql = "INSERT INTO passwords (title, url, username, password, notes, addUser, addDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedPassword.getTitle());
            statement.setString(2, selectedPassword.getUrl());
            statement.setString(3, selectedPassword.getUsername());
            statement.setString(4, AESUtil.encrypt(selectedPassword.getPassword()));
            statement.setString(5, selectedPassword.getNotes());

            statement.setString(6, selectedPassword.getAuditInfo().getAddUser());
            statement.setString(7, formattedDate); // Tarihi string olarak kaydet

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {

                LOGGER.log(Level.INFO, "Şifre veritabanına başarıyla kaydedildi.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password saved successfully."));
                // Yeni şifre kaydedildiğinde log girişi ekleyin
                logMB.addLogEntry(userSession.getUsername(), "yeni şifre eklendi: " + selectedPassword.getTitle());
                loadPasswordsFromDatabase();
                return "index?faces-redirect=true";
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Parola veritabanına kaydedilemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            // Hata durumunda log girişi ekleyin
            logMB.addLogEntry("Sisteme", "şifre kaydedilirken hata oluştu: " + e.getMessage());
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

                // Silinen şifre için log girişi ekle
                logMB.addLogEntry(userSession.getUsername(), "Şifre silindi: " + password.getTitle());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanından parola silinemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            logMB.addLogEntry("Sistemden", "şifre silinemedi: " + e.getMessage());
        }
    }

    public void updatePassword(Passwords password) {
        LOGGER.log(Level.INFO, "updatePassword methodu çağırıldı.");
        if (password == null) {
            LOGGER.log(Level.WARNING, "Güncelleme için şifre verilmedi.");
            return;
        }

        if (selectedPassword != null) {
            LOGGER.log(Level.INFO, "Şifre {0} güncelleniyor.", selectedPassword.getTitle());
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(auditInfo.getUpdDate()); // Tarihi "dd.MM.yyyy" formatına dönüştür
        auditInfo.setUpdDate(auditInfo.getUpdDate());
        LOGGER.log(Level.INFO, "Güncelleme kullanıcısı {0} olarak ayarlandı ve güncelleme tarihi {1}", new Object[]{auditInfo.getUpdUser(), formattedDate});

        String sql = "UPDATE passwords SET title=?, url=?, username=?, password=?, notes=?, updUser=?, updDate=? WHERE id=?";

        try ( Connection connection = DBConnection.getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password.getTitle());
            statement.setString(2, password.getUrl());
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
                logMB.addLogEntry(userSession.getUsername(), "Şifre güncellendi: " + password.getTitle());
            } else {
                LOGGER.log(Level.WARNING, "veritabanında güncellenen satır yok!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Veritabanındaki parola güncellenemedi.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Şifre güncellenemedi: " + e.getMessage()));
            logMB.addLogEntry("Sistemden", "şifre güncellenemedi: " + e.getMessage());
        }
    }

    public void prepareUpdate(Passwords password) {
        this.selectedPassword = password;

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
