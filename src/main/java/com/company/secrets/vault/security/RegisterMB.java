/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrets.vault.security;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author hatice.kemence
 */
@ManagedBean
@SessionScoped
public class RegisterMB {
    private String username;
    private String password;
    private String confirmPassword;

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
    
    public String register() {
    if (!password.equals(confirmPassword)) {
        // Parolalar eşleşmiyorsa
        // Hata mesajı göster
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords do not match!"));
        return null; // Kayıt başarısız olduğu için null döndür
    }
    
    // Parolalar eşleşiyorsa
    UserDAO userDAO = new UserDAO();
    userDAO.registerUser(username, password);
    return "login.xhtml?faces-redirect=true"; // Kayıt başarılı olduğu için giriş sayfasına yönlendir
}

   
}

    
    

