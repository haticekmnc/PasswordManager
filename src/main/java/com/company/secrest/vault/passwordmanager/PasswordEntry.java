/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrest.vault.passwordmanager;

import java.io.Serializable;

/**
 *
 * @author hatice.kemence
 */
public class PasswordEntry implements Serializable{
    private String title;
    private String url;
    private String username;
    private String password;
    private String notes;
    
    public PasswordEntry(String title, String url, String username, String password, String notes){
        this.title=title;
        this.url=url;
        this.username=username;
        this.password=password;
        this.notes=notes;
    }
    
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
   
    
}
