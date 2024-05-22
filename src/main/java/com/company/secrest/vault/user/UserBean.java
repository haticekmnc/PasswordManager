package com.company.secrest.vault.user;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;


import javax.inject.Inject;
import javax.inject.Named;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {

    private List<User> users;
    private boolean isAnyPasswordShowing = false; // Açık olan herhangi bir şifre var mı?
    

    @Inject
    private UserMB userMB;
    
   

    public UserBean() {
        // Constructor
    }

    public List<User> getUsers() {
        if (users == null || users.isEmpty()) {
            userMB.loadUsers();
            users = userMB.getUsers();
        }
        return users;
    }

    //kullanıcı listesini güncelleyen metod
    public void reloadUsers() {
        userMB.loadUsers();
        users = userMB.getUsers();
    }

    public UserMB getUserMB() {
        return userMB;
    }

    public void setUserMB(UserMB userMB) {
        this.userMB = userMB;
    }

    public void toggleShowPassword(User user, boolean manuallyTriggered) {
        if (manuallyTriggered || user.isShowPassword()) {
            if (!isAnyPasswordShowing) {
                user.setShowPassword(!user.isShowPassword());
                isAnyPasswordShowing = user.isShowPassword();
   
              
               
            } else if (isAnyPasswordShowing && user.isShowPassword()) {
                user.setShowPassword(false);
                isAnyPasswordShowing = false;
            }
        }
    }
    
    
    
  

    
}
