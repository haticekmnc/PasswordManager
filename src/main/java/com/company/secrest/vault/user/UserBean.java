package com.company.secrest.vault.user;

import com.company.secrest.vault.log.LogMB;
import com.company.secrest.vault.password.UserSession;
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
    private User selectedUser;

    @Inject
    private UserMB userMB;

    @Inject
    private UserSession userSession;

    @Inject
    private LogMB logMB;

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

    public void toggleShowPassword(User user, boolean manuallyTriggered) {
        if (manuallyTriggered || user.isShowPassword()) {
            if (!isAnyPasswordShowing) {
                user.setShowPassword(!user.isShowPassword());
                isAnyPasswordShowing = user.isShowPassword();

                // Log the user action
                String logMessage = String.format("%s kullanıcı kaydını görüntüledi", userSession.getUsername());
                logMB.addLogEntryForUser(userSession.getUsername(), logMessage, user.getId());                                          
            } else if (isAnyPasswordShowing && user.isShowPassword()) {
                user.setShowPassword(false);
                isAnyPasswordShowing = false;
            }
        }
    }

    // getters and setters
    public UserMB getUserMB() {
        return userMB;
    }

    public void setUserMB(UserMB userMB) {
        this.userMB = userMB;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

}
