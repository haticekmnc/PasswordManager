
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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

    @PostConstruct
    public void init() {
        loadPasswordsFromDatabase();
        timerService = new TimerService(); // timer başlatma
        // Test the timer service with a simple scheduled task
    timerService.schedule(() -> {
        System.out.println("Timer service test: This should appear in logs after 5 seconds.");
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
                    .filter(p -> p.getTitle().contains(filterText)
                    || p.getUrl().contains(filterText)
                    || p.getUsername().contains(filterText)
                    || p.getNotes().contains(filterText))
                    .collect(Collectors.toList());
        }
    }

    public void toggleShowPassword(Passwords password, boolean manuallyTriggered) {
        System.out.println("toggleShowPassword called. Manually triggered: " + manuallyTriggered + ", Current show password state: " + password.isShowPassword());
        if (manuallyTriggered || password.isShowPassword()) {
            password.setShowPassword(!password.isShowPassword());
            System.out.println("Password show state changed to: " + password.isShowPassword());
            if (password.isShowPassword()) {
                System.out.println("Scheduling password hide in 3 seconds.");
                schedulePasswordHide(password, 3); // 3 saniye sonra şifreyi otomatik olarak gizle
            }
        }
    }
// schedulePasswordHide metodu süre parametresi alacak şekilde güncelleniyor

  private void schedulePasswordHide(final Passwords password, int delaySeconds) {
    timerService.schedule(() -> {
        password.setShowPassword(false);
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("passwordForm:dataTable");
        System.out.println("Password visibility now hidden for: " + password.getTitle());
    }, delaySeconds * 1000);
}



    public void prepareForVerification(Passwords password) {
        this.selectedPassword = password;
        System.out.println("Preparing for verification: " + password.getTitle());
    }

   public void verifyAndToggleShowPassword() {
    System.out.println("Verifying user credentials. Username: " + verifyUsername);
    if (verifyUsername.equals(userSession.getUsername()) && verifyPassword.equals(userSession.getPassword())) {
        System.out.println("Verification successful.");
        toggleShowPassword(selectedPassword, true); // Şifre göster ve otomatik gizleme için timer başlat
    } else {
        System.out.println("Verification failed. Incorrect username or password.");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure", "Authentication failed!"));
    }
}
   


}
