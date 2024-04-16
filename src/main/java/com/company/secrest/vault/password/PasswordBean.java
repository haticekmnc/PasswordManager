import com.company.secrest.vault.password.PasswordManager;
import com.company.secrest.vault.password.Passwords;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named("passwordBean")
@ViewScoped
public class PasswordBean implements Serializable {
    private List<Passwords> passwords;
    private Passwords selectedPassword;
    private String filterText;
    private boolean isSuccess;

    @Inject
    private PasswordManager passwordManager;

    @PostConstruct
    public void init() {
        loadPasswordsFromDatabase();
    }

    public void loadPasswordsFromDatabase() {
        passwords = passwordManager.loadPasswordsFromDatabase();
    }

   

    public List<Passwords> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<Passwords> passwords) {
        this.passwords = passwords;
    }

    public Passwords getSelectedPassword() {
        return selectedPassword;
    }

    public void setSelectedPassword(Passwords selectedPassword) {
        this.selectedPassword = selectedPassword;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
        filterPasswords();
    }

    public PasswordManager getPasswordManager() {
        return passwordManager;
    }

    public void setPasswordManager(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    

    private void filterPasswords() {
        if (filterText == null || filterText.isEmpty()) {
            this.passwords = passwordManager.loadPasswordsFromDatabase();
        } else {
            this.passwords = passwordManager.loadPasswordsFromDatabase().stream()
                .filter(p -> p.getTitle().contains(filterText)
                          || p.getUrl().contains(filterText)
                          || p.getUsername().contains(filterText)
                          || p.getNotes().contains(filterText))
                .collect(Collectors.toList());
        }
    }
    
    






}
