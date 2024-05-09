
import com.company.secrest.vault.password.UserSession;
import com.company.secrest.vault.password.PasswordManager;
import com.company.secrest.vault.password.Passwords;
import com.company.secrets.vault.security.authentication.service.TimerService;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import log.LogMB;




@Named("passwordBean")
@ViewScoped

public class PasswordBean implements Serializable {

    private List<Passwords> passwords;
    private Passwords selectedPassword;
    private String filterText;
    private boolean isSuccess;
    private String verifyUsername;
    private String verifyPassword;

    private TimerService timerService; // zamanlayıcı için

    @Inject
    private PasswordManager passwordManager;

    @Inject
    private UserSession userSession; // Kullanıcı oturum bilgileri için
    @Inject
    private LogMB logMB;
    
   

    @PostConstruct
    public void init() {
        loadPasswordsFromDatabase();
        timerService = new TimerService(); // timer başlatma
        // Test the timer service with a simple scheduled task
        timerService.schedule(() -> {
            System.out.println("Zamanlayıcı servis testi: Bu, 5 saniye sonra günlüklerde görünmelidir.");
        }, 5000);
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

    public String getVerifyUsername() {
        return verifyUsername;
    }

    public void setVerifyUsername(String verifyUsername) {
        this.verifyUsername = verifyUsername;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    private void filterPasswords() {
        if (filterText == null || filterText.isEmpty()) {
            this.passwords = passwordManager.loadPasswordsFromDatabase();
        } else {
            this.passwords = passwordManager.loadPasswordsFromDatabase().stream()
                    .filter(p -> p.getSystemInformation().contains(filterText)
                    || p.getAccessInformation().contains(filterText)
                    || p.getUsername().contains(filterText)
                    || p.getNotes().contains(filterText))
                    .collect(Collectors.toList());
        }
    }

    private boolean isAnyPasswordShowing = false; // Açık olan herhangi bir şifre var mı?

public void toggleShowPassword(Passwords password, boolean manuallyTriggered) {
    if (manuallyTriggered || password.isShowPassword()) {
        if (!isAnyPasswordShowing) {
            password.setShowPassword(!password.isShowPassword());
            isAnyPasswordShowing = password.isShowPassword();

            /// Log the message
            String logMessage = String.format("%s şifresini görüntüledi: %s", userSession.getUsername(), password.getSystemInformation());
            logMB.addLogEntry(userSession.getUsername(), logMessage, password.getId());
            schedulePasswordHide(password, 3); // 3 saniye sonra şifreyi otomatik olarak gizle
        } else if (isAnyPasswordShowing && password.isShowPassword()) {
            password.setShowPassword(false);
            isAnyPasswordShowing = false;
        }
    }
}


private void schedulePasswordHide(final Passwords password, int delaySeconds) {
    timerService.schedule(() -> {
        password.setShowPassword(false);
        isAnyPasswordShowing = false;
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("passwordForm:dataTable");
    }, delaySeconds * 1000);
}


    public void prepareForVerification(Passwords password) {
        this.selectedPassword = password;
        System.out.println("Doğrulama için hazırlanıyor: " + password.getSystemInformation());
    }

    
}
