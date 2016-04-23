package com.epam.cdpSelenium.businessObject.users;

import com.epam.cdpSelenium.ui.pages.InboxPage;
import com.epam.cdpSelenium.ui.pages.SignInPage;
import org.openqa.selenium.WebDriver;

public class User {
    private String username;
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public InboxPage loginToGmail(WebDriver driver){
        SignInPage signInPage = new SignInPage(driver);
        return signInPage.signIn(this.getUsername(), this.getPassword());
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
