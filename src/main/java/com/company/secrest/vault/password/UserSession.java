package com.company.secrest.vault.password;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserSession implements Serializable {
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
    }

    public void logoutUser() {
        setUsername(null);
        setPassword(null);
    }
}
