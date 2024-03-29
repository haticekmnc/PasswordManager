package com.company.secrets.vault.security;

import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class LoginMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private static final String URL = "jdbc:sqlite:C:\\Users\\hatice.kemence\\Desktop\\mydatabase\\haticeDatabase.db";
    private boolean loginError;

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

    public boolean isLoginError() {
        return loginError;
    }

    public void setLoginError(boolean loginError) {
        this.loginError = loginError;
    }

    public String login() {
        String dbUsername = "";
        String dbPassword = "";
        
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT username, password FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        dbUsername = resultSet.getString("username");
                        dbPassword = resultSet.getString("password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (username.equals(dbUsername) && password.equals(dbPassword)) {
            return "index.xhtml?faces-redirect=true";
        } else {
            loginError = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error", "Invalid username or password."));
            return null;
        }
    }
    
     
}
