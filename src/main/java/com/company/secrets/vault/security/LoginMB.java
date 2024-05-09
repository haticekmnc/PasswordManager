package com.company.secrets.vault.security;


import message.MessageView;
import com.company.secrest.vault.password.UserSession;
import com.company.secrets.vault.security.authentication.service.AuthenticationService;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import log.LogMB;


@Named("loginMB")
@SessionScoped
public class LoginMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private Integer userId;
    private boolean loginError;
    private boolean isAdmin; //Admin kontrolü için
    
    // Sabit admin bilgileri
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";


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
        // Önce admin kontrolü yapılıyor
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            isAdmin = true; // Kullanıcı admin
            userSession.loginUser(username, password, userId); // Admin bilgileri session'a kaydet
            return "index.xhtml?faces-redirect=true"; // Admin anasayfasına yönlendir
        }

        // Normal kullanıcılar için kimlik doğrulama
        if (authService.authenticate(username, password)) {
            isAdmin = false; // Kullanıcı admin değil
            userSession.loginUser(username, password, userId); // Kullanıcı bilgilerini session'a kaydet
            messageView.showSuccess("Başarılı Giriş", "Hoşgeldiniz, " + username + "!");
            logMB.addLogEntry(userSession.getUsername(), "kullanıcısı sisteme başarıyla giriş yaptı!", serialVersionUID);
            return "index.xhtml?faces-redirect=true"; // Başarılı giriş
        } else {
            loginError = true;
            messageView.showFailure("Giriş hatası", "Geçersiz kullanıcı adı ve şifre!");
            return "login.xhtml?faces-redirect=true"; // Başarısız giriş
        }
    }

    // Logout method
    public String logout() {
        userSession.logoutUser(); // Kullanıcı oturumunu sonlandır
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Oturum verilerini temizle
        username = null; // Username alanını temizle
        password = null; // Password alanını temizle
        messageView.showInfo("Başarılı Çıkış", "Başarıyla çıkış yaptınız.");
        logMB.addLogEntry(userSession.getUsername(), "kullanıcısı sistemden başarıyla çıkış yaptı!", serialVersionUID);
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    
}