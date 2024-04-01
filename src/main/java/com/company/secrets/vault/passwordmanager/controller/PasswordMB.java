
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
    private String selectedFilterColumn;
    private String filterText;
    private List<PasswordEntry> filteredPasswords;
    private boolean isSuccess; //Başarılı kayıt kontrolü için boolean değişken

    public List<PasswordEntry> getFilteredPasswords() {
        return filteredPasswords;
    }

    public void setFilteredPasswords(List<PasswordEntry> filteredPasswords) {
        this.filteredPasswords = filteredPasswords;
    }
    
    

    public String getSelectedFilterColumn() {
        return selectedFilterColumn;
    }

    public void setSelectedFilterColumn(String selectedFilterColumn) {
        this.selectedFilterColumn = selectedFilterColumn;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }
    
    

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

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    

    @PostConstruct
    public void init() {
        // Şifreleri veritabanından yükle
        loadPasswordsFromDatabase();
    }

    public void loadPasswordsFromDatabase() {
        passwords = new ArrayList<>();

        try ( Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db")) {
            String sql = "SELECT * FROM passwords";
            try ( PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    PasswordEntry entry = new PasswordEntry(
                            resultSet.getString("title"),
                            resultSet.getString("url"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("notes")
                    );
                    entry.setId(resultSet.getLong("id"));  // id değerini atama
                    passwords.add(entry);
                }
                
                // Filtreleme için
                filteredPasswords = new ArrayList<>(passwords);

            }
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", e.getMessage()));

        }

    }
    
    public void filterPasswords(){
        filteredPasswords = passwords.stream()
                .filter(password -> password.getTitle().contains(filterText)
                        || password.getUsername().contains(filterText)
                        || password.getNotes().contains(filterText))
                .collect(Collectors.toList());
                
        
    }

    public String savePassword() {
        
        
        if (title == null || title.isEmpty() || password == null || password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Başlık ve şifre alanları zorunludur."));
            return null;
        }

        try ( Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db")) {
            String sql = "INSERT INTO passwords (title, url, username, password, notes) VALUES (?, ?, ?, ?, ?)";
            try ( PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, title);
                statement.setString(2, url);
                statement.setString(3, username);
                statement.setString(4, password);
                statement.setString(5, notes);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Şifre başarıyla kaydedildi."));
                    loadPasswordsFromDatabase();
                    resetForm();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Şifre eklenemedi."));
                }
                
            }
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", e.getMessage()));
        }

        return null;
        
        
    }

    public String deletePassword(PasswordEntry passwordEntry) {
        System.out.println("deletePassword() metoduna gelen PasswordEntry: " + passwordEntry.toString());

        Long id = passwordEntry.getId();
        if (id == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Şifre ID değeri null."));
            return null;
        }

        try ( Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db")) {
            String sql = "DELETE FROM passwords WHERE id = ?";
            try ( PreparedStatement statement = connection.prepareStatement(sql)) {
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
            }
        } catch (SQLException e) {
            System.out.println("Hata: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", e.getMessage()));
        }
        return null;
    }

    public void updatePassword() {
    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db")) {
        String sql = "UPDATE passwords SET title=?, url=?, username=?, password=?, notes=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, url);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setString(5, notes);
            statement.setLong(6, selectedPassword.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Şifre başarıyla güncellendi."));
                loadPasswordsFromDatabase();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Şifre güncellenemedi."));
            }
        }
    } catch (SQLException e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", e.getMessage()));
    }
}


    public void prepareUpdate(PasswordEntry passwordEntry) {
    this.selectedPassword = passwordEntry;
    this.title = passwordEntry.getTitle();
    this.url = passwordEntry.getUrl();
    this.username = passwordEntry.getUsername();
    this.password = passwordEntry.getPassword();
    this.notes = passwordEntry.getNotes();
}


    private void resetForm() {
        title = null;
        url = null;
        username = null;
        password = null;
        confirmPassword = null;
        notes = null;
        selectedPassword = null;
        isSuccess = false; //flag' i sıfırla
    }
}
