
import com.company.secrets.vault.security.authentication.service.AuthenticationService;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LoginMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean loginError;
    
    @ManagedProperty(value="#{authenticationService}")
    private AuthenticationService authService;
    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }

    
    //non-arg constructor
    public LoginMB(){
        
    }

    // Constructor
    public LoginMB(AuthenticationService authService) {
        this.authService = authService;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isLoginError() { return loginError; }
    public void setLoginError(boolean loginError) { this.loginError = loginError; }

    // Login method
    public String login() {
        if (authService.authenticate(username, password)) {
            return "index.xhtml?faces-redirect=true";
        } else {
            loginError = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error", "Invalid username or password."));
            return null;
        }
    }
}
