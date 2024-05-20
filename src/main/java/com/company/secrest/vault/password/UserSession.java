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
    private Integer userId; // Kullanıcıya özgü benzersiz ID
    private boolean isAdmin; // Kullanıcı admin mi?

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    

    public void loginUser(String username, String password ,Integer userId) {
        setUsername(username);
        setPassword(password);
        setUserId(userId); // User ID' yi de sessiona kaydet
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
        this.userId = null;  // Oturumu sonlandırırken User ID'yi de temizle
    } catch (Exception e) {
        LOGGER.severe("Çıkış sırasında hata: " + e.getMessage());
    }
}



}
