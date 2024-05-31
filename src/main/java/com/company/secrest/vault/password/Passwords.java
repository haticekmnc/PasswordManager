package com.company.secrest.vault.password;

import com.company.secrest.vault.entity.AuditInfo;

public class Passwords {
    private Long id;
    private String systemInformation;
    private String accessInformation;
    private String username;
    private String password; // Bu alan, kullanıcı tarafından girilen ham şifreyi temsil ediyor olmalı
    private String confirmPassword; // Bu alanın kullanımı genellikle UI tarafında şifre tekrarını doğrulamak içindir ve veritabanına kaydedilmemeli
    private String notes;
    private boolean showPassword; // UI'da şifrenin gösterilip gösterilmeyeceğini kontrol eder
    private String email;
    private AuditInfo auditInfo;
    
   
    
    
    
    
    public Passwords(String systemInformation, String accessInformation, String username, String password, String notes) {
        this.systemInformation = systemInformation;
        this.accessInformation = accessInformation;
        this.username = username;
        this.password = password; // Kullanıcı tarafından girilen ham şifre
        this.notes = notes;
    }

    
    public Passwords() {
    // Boş constructor
}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemInformation() {
        return systemInformation;
    }

    public void setSystemInformation(String systemInformation) {
        this.systemInformation = systemInformation;
    }

    public String getAccessInformation() {
        return accessInformation;
    }

    public void setAccessInformation(String accessInformation) {
        this.accessInformation = accessInformation;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    // DECRYPT , ENCRYPT İŞLEMLERİ
    public String getDecryptedPassword() {
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