package com.company.secrest.vault.password;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;


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
        if (username != null || password != null) {
            LOGGER.info("Kullanıcı çıkış yaptı: " + username);  // Seviyeyi INFO yapın
            this.username = null;
            this.password = null;
        } else {
            LOGGER.info("Çıkış yapacak kullanıcı yok.");
        }
    } catch (Exception e) {
        LOGGER.severe("Çıkış sırasında hata: " + e.getMessage());
    }
}



}
