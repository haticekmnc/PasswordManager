/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrest.vault.passwordmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author hatice.kemence
 */
@ManagedBean
@ViewScoped
public class PasswordMB implements Serializable{
    
    private String title;
    private String url;
    private String username;
    private String password;
    private String confirmPassword;
    private String notes;
    private List<PasswordEntry> passwordList;
    
    // Getter and setter methods

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
    
    public List<PasswordEntry> getPasswordList(){
        if (passwordList == null) {
            passwordList = new ArrayList<>();
        }
        return passwordList;
    }
    
    public void setPasswordList(List<PasswordEntry> passwordList) {
        this.passwordList = passwordList;
    }
    
    
    
    // Action method for saving password
public String savePassword(){
    // Check if passwords match
    if (!password.equals(confirmPassword)){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password and confirm password do not match", null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return null; // Stay on the same page
    }
    
    getPasswordList().add(new PasswordEntry(title, url, username, password, notes));
    
    title = "";
    url = "";
    username = "";
    password = "";
    confirmPassword = "";
    notes = "";
    
    
    // Redirect to index.xhtml after saving
    return "index.xhtml?faces-redirect=true";
}
     public void editPassword(PasswordEntry password){
         // düzenleme işlevselliği buraya eklenecek
     }
     
     public void deletePassword(PasswordEntry password){
         getPasswordList().remove(password);
     }

}
