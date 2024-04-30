
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
import log.LogMB;
import org.primefaces.PrimeFaces;

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

   public void toggleShowPassword(Passwords password, boolean manuallyTriggered) {
    System.out.println("toggleShowPassword çağrıldı. Manuel olarak tetiklenen: " + manuallyTriggered + ", Geçerli parolayı göster durumu: " + password.isShowPassword());
    if (manuallyTriggered || password.isShowPassword()) {
        password.setShowPassword(!password.isShowPassword());
        System.out.println("Parola gösterme durumu şu şekilde değişti: " + password.isShowPassword());
        if (password.isShowPassword()) {
            System.out.println("Zamanlama şifresi 3 saniye içinde gizlenir.");
            schedulePasswordHide(password, 3); // 3 saniye sonra şifreyi otomatik olarak gizle
            PrimeFaces.current().executeScript("hidePasswordAutomatically(3);"); // JavaScript fonksiyonunu çağırarak parolanın otomatik olarak gizlenmesini sağla
        }
    }
}



// schedulePasswordHide metodu süre parametresi alacak şekilde güncelleniyor
   // schedulePassword func ile belli bi süre sonra şifre gizlenir

  private void schedulePasswordHide(final Passwords password, int delaySeconds) {
    timerService.schedule(() -> {
        password.setShowPassword(false);
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("passwordForm:dataTable");
        System.out.println("Şifre görünürlüğü artık şunlar için gizli: " + password.getSystemInformation());
    }, delaySeconds * 1000);
}



    public void prepareForVerification(Passwords password) {
        this.selectedPassword = password;
        System.out.println("Doğrulama için hazırlanıyor: " + password.getSystemInformation());
    }

   public void verifyAndToggleShowPassword() {
    System.out.println("Kullanıcı kimlik bilgileri doğrulanıyor. Kullanıcı adı: " + verifyUsername);
    if (verifyUsername.equals(userSession.getUsername()) && verifyPassword.equals(userSession.getPassword())) {
        System.out.println("Doğrulama başarılı.");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Şifre başarıyla açıldı"));
                // Yeni şifre kaydedildiğinde log girişi ekleyin
                logMB.addLogEntry(userSession.getUsername(), "Şifreyi başarıyla açtı!");
        // Seçili şifreyi göstermek ve otomatik olarak gizlemek için toggleShowPassword metodunu çağırın
        toggleShowPassword(selectedPassword, true);
        // Şifrenin otomatik olarak gizlenmesi için zamanlayıcıyı başlatın
        schedulePasswordHide(selectedPassword, 3);
    } else {
        System.out.println("Doğrulama başarısız oldu. Yanlış kullanıcı adı veya şifre.");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "BAŞARISIZ", "Kimlik doğrulama başarısız!"));
        logMB.addLogEntry(userSession.getUsername(), "şifreyi açamadı!"+ " "+"Yanlış kullanıcı adı veya şifre!" );
        
    }
}




}
