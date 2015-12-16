package com.epam.cdpSelenium.ui.pages;

import com.epam.cdpSelenium.businessObject.users.User;
import com.epam.cdpSelenium.utils.UtilsMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class InboxPage {
    private final String SPAM_URL = "https://mail.google.com/mail#spam";
    private final String LOGOUT_URL = "https://mail.google.com/mail/logout";
    private WebDriver driver;
    Logger logger = LogManager.getRootLogger();

    @FindBy(xpath = "//div[text()='COMPOSE']")
    private WebElement buttonCompose;

    @FindBy(xpath = "//tbody/tr/td[6]")
    List<WebElement> emailTitles;

    @FindBy(xpath = "//a[contains(text(), 'Inbox')]")
    WebElement inboxButton;

    public InboxPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String createMessageTo (WebDriver driver, User user){
        buttonCompose.click();
        ComposeMessagePage composeMessagePage = new ComposeMessagePage(driver);
        composeMessagePage.sendSampleMessage(driver, user);
        return composeMessagePage.getUniqueMessageTitle();
    }

    public void searchForMessageInInboxAndMarkAsSpam (WebDriver driver, String title) {

        for (WebElement element : emailTitles) { //search in Inbox
            if (element.getText().contains(title)) {
                logger.info("Message '" + title + "' found in Inbox");
                element.click();
                UtilsMethods.getWait().until(ExpectedConditions.titleContains(title));
                MessagePage messagePage = new MessagePage(driver);
                messagePage.markLetterAsSpam(driver);
                return;
            }
        }
        logger.warn("Didn't get a message");
        UtilsMethods.takeScreenshot(driver, "ConfirmNoTestMessageInInbox");
    }

    public Boolean searchForMessageInSpamAndUnspam (WebDriver driver, String title) {
            SpamPage spamPage = goToSpam(driver);
            return spamPage.searchForMessageAndUnspam(title);
    }

    public SpamPage goToSpam(WebDriver driver) {
        driver.get(SPAM_URL);
        try {
            UtilsMethods.getWait().until(ExpectedConditions.titleContains("Spam"));
            logger.info("Spam page is loaded");
        } catch (Exception e) {
            logger.warn("Unable to go to spam page, " + e.getMessage());
            UtilsMethods.takeScreenshot(driver, "WasntAbleToLoadSpam");
        }
        SpamPage spamPage = new SpamPage(driver);
        return spamPage;
    }

    public void logout(WebDriver driver) {
        driver.get(LOGOUT_URL);
        try {
            UtilsMethods.getWait().until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            logger.info("No alert this time");
        }
        try {
            UtilsMethods.getWait().until(ExpectedConditions.urlContains("accounts.google.com"));
            logger.info("Logged out");
        } catch (Exception e) {
            logger.warn("Unable to log out, " + e.getMessage());
            UtilsMethods.takeScreenshot(driver, "WasntAbleToLogOut");
        }
    }

    public List<WebElement> getEmailTitles() {
        return emailTitles;
    }

}
