import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * PasswordEntry sınıfı, şifre yönetimi için kullanılan bir modeldir.
 * Bu sınıf, bir şifre girişinin özelliklerini ve metotlarını içerir.
 */
@ManagedBean(name="passwordEntry")
@SessionScoped
public class PasswordEntry implements Serializable {

    private Long id;
    private String title;
    private String url;
    private String username;
    private String hashedPassword; // Şifrelenmiş şifre
    private String notes;
    private String decryptedPassword; // Açık metin şifre, güvenlik nedeniyle dikkatli kullanılmalı

    private boolean showPassword; // Şifreyi UI'da gösterip göstermeme durumu

    // Boş constructor
    public PasswordEntry() {
    }

    // Parametreli constructor
    public PasswordEntry(String title, String url, String username, String hashedPassword, String notes) {
        this.title = title;
        this.url = url;
        this.username = username;
        this.hashedPassword = hashedPassword; // Düzeltildi: hashedPassword yerine password parametresi atanmalıydı.
        this.notes = notes;
    }

    // Getter ve Setter metotları
    public Long getId() {
        return id;
    }

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
        return hashedPassword;
    }

    public void setPassword(String password) {
        this.hashedPassword = password;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }

    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }

    // toString metodu üzerinden nesne bilgilerini string olarak döndürme
    @Override
    public String toString() {
        return "PasswordEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
