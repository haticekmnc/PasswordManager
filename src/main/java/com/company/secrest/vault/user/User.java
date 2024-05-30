package com.company.secrest.vault.user;

import com.company.secrest.vault.entity.AuditInfo;
import com.company.secrest.vault.password.AESUtil;
import java.io.Serializable;



public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private boolean showPassword; // UI'da şifrenin gösterilip gösterilmeyeceğini kontrol eder
     private AuditInfo auditInfo;
     
     public User(){
         
     }

    public User(String username, String password, String email, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }


    public Long getId() {
        return id;
    }

    // Getters and setters
    public void setId(Long id) {    
        this.id = id;
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

    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }  

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }
    
    
    // DECRYPT , ENCRYPT İŞLEMLERİ
    public String getDecryptedUserPassword() {
        try{
            return AESUtil.decrypt(this.password);
        } catch (Exception e) {
            System.err.println("Şifre deşifreleme hatası: " + e.getMessage());
            return null;
        }
        
    }

    public void encryptAndSetPassword(String password) {
        this.password = AESUtil.encrypt(password);
    }
    
    
}