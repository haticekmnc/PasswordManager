package com.company.secrest.vault.password;

import entity.AuditInfo;

public class Passwords {

    private Long id;
    private String title;
    private String url;
    private String username;
    private String password; // Bu alan, kullanıcı tarafından girilen ham şifreyi temsil ediyor olmalı
    private String confirmPassword; // Bu alanın kullanımı genellikle UI tarafında şifre tekrarını doğrulamak içindir ve veritabanına kaydedilmemeli
    private String notes;
    private boolean showPassword; // UI'da şifrenin gösterilip gösterilmeyeceğini kontrol eder
    
    private AuditInfo auditInfo;
    
    public Passwords(String title, String url, String username, String password, String notes) {
        this.title = title;
        this.url = url;
        this.username = username;
        this.password = password; // Kullanıcı tarafından girilen ham şifre
        this.notes = notes;
    }

    public Passwords() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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