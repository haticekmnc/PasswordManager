package com.company.secrest.vault.password;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@Named
@SessionScoped
public class UserSession implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(UserSession.class.getName());
    private String username;
    private String password;
    
    

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

    public void loginUser(String username, String password) {
        setUsername(username);
        setPassword(password);
         LOGGER.info("User logged in: " + username);
         //LOGGER.fine("Kullanıcı giriş yaptı: " + username);
    }

    public void logoutUser() {
    try {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate(); // JSF oturumunu sonlandır
            LOGGER.info("Kullanıcı çıkış yaptı: " + username);
        }
        this.username = null;
        this.password = null;
    } catch (Exception e) {
        LOGGER.severe("Çıkış sırasında hata: " + e.getMessage());
    }
}



}
