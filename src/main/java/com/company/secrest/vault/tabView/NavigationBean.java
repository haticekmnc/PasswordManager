package com.company.secrest.vault.tabView;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class NavigationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currentPage;

    @PostConstruct
    public void init() {
        currentPage = "/passwordList.xhtml"; // Uygulama ilk açıldığında bu sayfa yüklenecek
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void showPasswordList() {
        currentPage = "/passwordList.xhtml";
    }

    public void showLogPage() {
        currentPage = "/logPage.xhtml";
    }

    public void showRecords() {
        currentPage = "/userRecords.xhtml";
    }
}
