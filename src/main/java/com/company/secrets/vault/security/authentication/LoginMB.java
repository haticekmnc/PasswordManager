
import com.company.secrest.vault.password.UserSession;
import com.company.secrets.vault.security.authentication.service.AuthenticationService;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import log.LogMB;

@Named
@SessionScoped
public class LoginMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean loginError;

    @Inject
    private AuthenticationService authService;

    @Inject
    private UserSession userSession;

    @Inject
    private LogMB logMB;

    @Inject
    private MessageView messageView;

    // Login method
    public String login() {
        if (authService.authenticate(username, password)) {
            userSession.loginUser(username, password); // Kullanıcı bilgilerini session'a kaydet
            logMB.addLogEntry(username, "sisteme giriş yaptı."); // Log girişi ekleyin
            messageView.showSuccess("Başarılı Giriş", "Hoşgeldiniz, " + username + "!");
            return "index.xhtml?faces-redirect=true";
        } else {
            loginError = true;
            messageView.showFailure("Giriş hatası", "Geçersiz kullanıcı adı ve şifre!");
            return null;
        }
    }

    // Logout method
    public String logout() {
        logMB.addLogEntry(userSession.getUsername(), "sistemden çıkış yaptı.");
        userSession.logoutUser(); // Kullanıcı oturumunu sonlandır
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Oturum verilerini temizle
        username = null; // Username alanını temizle
        password = null; // Password alanını temizle
        messageView.showInfo("Başarılı Çıkış", "Başarıyla çıkış yaptınız.");
        return "/login.xhtml?faces-redirect=true"; // Login sayfasına yönlendir
    }

    // Getters and Setters
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

    public boolean isLoginError() {
        return loginError;
    }

    public void setLoginError(boolean loginError) {
        this.loginError = loginError;
    }
}
