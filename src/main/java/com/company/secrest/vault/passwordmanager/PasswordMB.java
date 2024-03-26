
import com.company.secrest.vault.passwordmanager.PasswordEntry;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import com.company.secrest.vault.passwordmanager.PasswordEntry;

@ManagedBean(name = "passwordMB")
@SessionScoped
public class PasswordMB implements Serializable {

    private Long id;
    private String title;
    private String url;
    private String username;
    private String password;
    private String confirmPassword;
    private String notes;
    private List<PasswordEntry> passwords;
    private PasswordEntry selectedPassword;

    public Long getId() {
        return id;
    }

    // Getter and Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PasswordEntry> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<PasswordEntry> passwords) {
        this.passwords = passwords;
    }

    public PasswordEntry getSelectedPassword() {
        return selectedPassword;
    }

    public void setSelectedPassword(PasswordEntry selectedPassword) {
        this.selectedPassword = selectedPassword;
    }

    @PostConstruct
    public void init() {
        // Şifreleri veritabanından yükle
        loadPasswordsFromDatabase();
    }

    public void loadPasswordsFromDatabase() {
        passwords = new ArrayList<>();

        try {
            // Veritabanı bağlantısı oluştur
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db");

            //SQL sorgusu oluştur
            String sql = "SELECT * FROM passwords";
            PreparedStatement statement = connection.prepareStatement(sql);

            //Sorguyu çalıştır ve sonuçları al
            ResultSet resultSet = statement.executeQuery();

            //Sonuçları dolaşarak PasswordEntry nesnelerini oluştur ve password listesine ekle
            while (resultSet.next()) {
                PasswordEntry entry = new PasswordEntry(title, url, username, password, notes);
                entry.setTitle(resultSet.getString("title"));
                entry.setUrl(resultSet.getString("url"));
                entry.setUsername(resultSet.getString("username"));
                entry.setPassword(resultSet.getString("password"));
                entry.setNotes(resultSet.getString("notes"));
                passwords.add(entry);
            }

            //Bağlantıyı kapat
            connection.close();
        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

    }

    public String savePassword() {
        if (title == null || title.isEmpty() || password == null || password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Başlık ve şifre alanları zorunludur."));
            return null;

        }

        try {
            // Veritabanı bağlantısı oluştur
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db");

            // SQL sorgusu oluştur
            String sql = "INSERT INTO passwords (title, url, username, password, notes) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, url);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setString(5, notes);

            // Sorguyu çalıştır
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // Kayıt başarıyla eklendi
                System.out.println("Kayıt başarıyla eklendi.");

                // Başarı mesajı göster
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Şifre başarıyla kaydedildi."));

                //Yeni şifreyi passwords listesine ekle
                PasswordEntry newEntry = new PasswordEntry(title, url, username, password, notes);
                newEntry.setTitle(title);
                newEntry.setUrl(url);
                newEntry.setUsername(username);
                newEntry.setPassword(password);
                newEntry.setNotes(notes);
                passwords.add(newEntry);

                // Formu temizle
                title = null;
                url = null;
                username = null;
                password = null;
                confirmPassword = null;
                notes = null;
            } else {
                // Kayıt ekleme başarısız oldu
                System.out.println("Kayıt ekleme başarısız oldu.");
                // Hata mesajı gösterilebilir
            }

            // Bağlantıyı kapat
            connection.close();
        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return null; // JSF navigasyonu için null döndür
    }

    public String deletePassword(Long id) {
        if (id == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Şifre ID değeri null."));
            return null;
        }

        try {
            // Veritabanı bağlantısı oluştur
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db");

            String sql = "DELETE FROM passwords WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Kayıt başarıyla silindi.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Şifre başarıyla silindi."));
                loadPasswordsFromDatabase(); // Şifreleri tekrar yükle
            } else {
                System.out.println("Kayıt silme başarısız oldu.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Şifre silinemedi."));
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", e.getMessage()));
        }
        return null;
    }

    public void updatePassword() {
        if (selectedPassword == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Lütfen güncellenecek şifreyi seçin."));
            return;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db");

            String sql = "UPDATE passwords SET title=?, url=?, username=?, password=?, notes=? WHERE title=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, url);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setString(5, notes);
            statement.setString(6, selectedPassword.getTitle());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Kayıt başarıyla güncellendi.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Şifre başarıyla güncellendi."));

                selectedPassword.setTitle(title);
                selectedPassword.setUrl(url);
                selectedPassword.setUsername(username);
                selectedPassword.setPassword(password);
                selectedPassword.setNotes(notes);

                title = null;
                url = null;
                username = null;
                password = null;
                confirmPassword = null;
                notes = null;
                selectedPassword = null;

            } else {
                System.out.println("Kayıt güncelleme başarısız oldu.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Şifre güncellenemedi."));

            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());

        }
    }

    public void prepareUpdate(PasswordEntry passwordEntry) {
        this.selectedPassword = passwordEntry;
        this.title = passwordEntry.getTitle();
        this.url = passwordEntry.getUrl();
        this.username = passwordEntry.getUsername();
        this.password = passwordEntry.getPassword();
        this.confirmPassword = passwordEntry.getPassword();
        this.notes = passwordEntry.getNotes();
    }
}
