/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrets.vault.security;

import com.company.secrest.vault.password.UserSession;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import com.company.secrest.vault.log.LogMB;
import com.company.secrest.vault.user.UserBean;

/**
 *
 * @author hatice.kemence
 */
@ManagedBean
@SessionScoped
public class RegisterMB implements Serializable {
    private static final long serialVersionUID = 1L;  //serileştirme sırasında uyumluluğu korumak için
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    
    
    @Inject
    private LoginMB loginMB; // LoginMB sınıfına erişim için
    
    @Inject
    private LogMB logMB;
    
    @Inject
    private UserSession userSession;
    
    @Inject
    private UserBean userBean;
    
    
      

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

    public String getConfirmPassword() {
    return confirmPassword;
}

public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    
   public String register() {
        // Yalnızca admin kullanıcılar kayıt yapabilsin
        if (!loginMB.getIsAdmin()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyarı", "Yalnızca admin kayıt ekleyebilir!"));
            return null; // Erişim reddedildi
        }

        if (!password.equals(confirmPassword)) {
            // Parolalar eşleşmiyorsa
            // Hata mesajı göster
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Şifreler eşleşmiyor!"));
            return null; // Kayıt başarısız olduğu için null döndür
        }
        
        // Parolalar eşleşiyorsa kullanıcıyı kaydet
        UserDAO userDAO = new UserDAO();
        boolean registerSuccess = userDAO.registerUser(username, password, email);

        if (registerSuccess) {
           

            // Başarılı kayıt için log girişi ekle ve başarı mesajı göster
            //logMB.addLog(username, "Yeni kullanıcı kaydı: " + username,Long.MAX_VALUE);
            logMB.addLogEntry(userSession.getUsername(), "Yeni kullanıcı kaydı ekledi.", Long.MAX_VALUE);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Kayıt başarılı!"));
            userBean.reloadUsers(); //kullanıcı kaydını yenile
            return "index.xhtml?faces-redirect=true"; // Kayıt başarılı olduğu için giriş sayfasına yönlendir
        } else {
            // Kayıt işlemi başarısız olduysa hata mesajı göster
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Kayıt başarısız oldu!"));
            return null;
        }
    }
}

    
    

