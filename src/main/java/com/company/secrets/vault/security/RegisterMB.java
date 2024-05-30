/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrets.vault.security;

import com.company.secrest.vault.entity.AuditInfo;
import com.company.secrest.vault.user.UserBean;
import com.company.secrest.vault.password.UserSession;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import com.company.secrest.vault.log.LogMB;
import com.company.secrest.vault.password.AESUtil;
import com.company.secrest.vault.user.User;
import java.util.Date;

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
    private boolean isAdmin;
    
    
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

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    


    
  public String register() {
    if (!loginMB.getIsAdmin()) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyarı", "Yalnızca admin kayıt ekleyebilir!"));
        return null;
    }
     FacesContext context = FacesContext.getCurrentInstance();
        // Log ekle
        System.out.println("Register metodu çağrıldı.");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Confirm Password: " + confirmPassword);
        System.out.println("Is Admin: " + isAdmin);

        // Parola ve confirmPassword eşleşmesi kontrolü
        if (!password.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", null));
            return null;
        }
        // Kullanıcı kaydı (burada veritabanına kayıt işlemi yapılabilir)
        // Örnek olarak FacesContext kullanılarak kullanıcıya mesaj gösteriliyor
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User registered successfully", null));   
        // Şifreyi şifrele
    String encryptedPassword = AESUtil.encrypt(password);
    if (encryptedPassword == null) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password encryption failed", null));
        return null;
    }
    // Yeni User nesnesi oluştur
    User newUser = new User(username, encryptedPassword, email, isAdmin);

    // AuditInfo nesnesi oluştur ve ayarla
    AuditInfo auditInfo = new AuditInfo();
    auditInfo.setAddUser(userSession.getUsername()); // Şu anki kullanıcı adını ayarla
    auditInfo.setAddDate(new Date()); // Şu anki tarihi ayarla
    newUser.setAuditInfo(auditInfo); // User nesnesine AuditInfo ekleyin

    // DAO metodunu güncelleyin
    UserDAO userDAO = new UserDAO();
    boolean registerSuccess = userDAO.registerUser(newUser);

    if (registerSuccess) {
        logMB.addLogEntry(userSession.getUsername(), "Yeni kullanıcı kaydı ekledi.", Long.MAX_VALUE);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Kayıt başarılı!"));
        userBean.reloadUsers();
        return "index.xhtml?faces-redirect=true";
    } else {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Kayıt başarısız oldu!"));
        return null;
    }
}
 
}

    
    
