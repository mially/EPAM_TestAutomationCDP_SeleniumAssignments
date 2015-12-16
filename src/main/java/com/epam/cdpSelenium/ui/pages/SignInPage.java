package com.epam.cdpSelenium.ui.pages;

import com.epam.cdpSelenium.utils.UtilsMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

public class SignInPage {
    private final String SIGN_IN_URL = "https://accounts.google.com/AddSession?continue=https://mail.google.com/";
    private final String INBOX_URL = "https://mail.google.com/mail/#inbox";
    private WebDriver driver;
    Logger logger = LogManager.getRootLogger();

    @FindBy(id = "Email")
    private List<WebElement> inputLogins;

    @FindBy(id = "Passwd")
    private WebElement inputPassword;

    @FindBy (id = "next")
    private WebElement buttonNext;

    @FindBy(id = "signIn")
    private WebElement buttonSubmit;

    @FindBy(id = "account-chooser-link")
    private List<WebElement> chooseAccounts;

    @FindBy(id = "account-chooser-add-account")
    private List<WebElement> addAccounts;

    public SignInPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public InboxPage signIn(String username, String password) {
        driver.get(SIGN_IN_URL);

        if ((inputLogins.isEmpty() || !inputLogins.get(0).isDisplayed()) && !chooseAccounts.isEmpty()) {
            WebElement chooseAccount = UtilsMethods.getWait().until(ExpectedConditions
                    .elementToBeClickable(getChooseAccounts().get(0)));
            try {
                UtilsMethods.highlightElement(driver, chooseAccount);
                chooseAccount.click();
                //safeJavaScriptClick(chooseAccount);
            } catch (Exception e) {
                logger.warn("Something is wrong: " + e.getMessage());
                UtilsMethods.takeScreenshot(driver);
            }
        }

        if ((inputLogins.isEmpty() || !inputLogins.get(0).isDisplayed()) && !addAccounts.isEmpty()) {
            WebElement addAccount = UtilsMethods.getWait().until(ExpectedConditions
                    .elementToBeClickable(getAddAccounts().get(0)));
            addAccount.click();
        }

        WebElement inputLogin = inputLogins.get(0);
        UtilsMethods.clear(inputLogin);
        inputLogin.sendKeys(username);
        try {
            buttonNext.click();
           // safeJavaScriptClick(buttonNext); //just trying JSExecutor - does not work with decorated webdriver!?
        } catch (Exception e) {
            logger.warn("Something is wrong: " + e.getMessage());
            UtilsMethods.takeScreenshot(driver);
        }

        UtilsMethods.clear(inputPassword);
        inputPassword.sendKeys(password);
        UtilsMethods.takeScreenshot(driver); //just4test
        buttonSubmit.click();
        return goToInbox();
    }

    public InboxPage goToInbox(){
        driver.get(INBOX_URL);
        try {
            UtilsMethods.getWait().until(ExpectedConditions.titleContains("Inbox"));
            logger.info("Logged in to "+ driver.getTitle());
        } catch (Exception e) {
            logger.warn("Unable to login: " + e.getMessage());
            UtilsMethods.takeScreenshot(driver);
        }
        return new InboxPage(driver);
    }

    public void safeJavaScriptClick(WebElement element) throws Exception {
        try {
            if (element.isEnabled() && element.isDisplayed()) {
                logger.info("Clicking on element with using java script click");

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } else {
                logger.warn("Unable to click on element");
                UtilsMethods.takeScreenshot(driver);
            }
        } catch (StaleElementReferenceException e) {
            logger.warn("Element is not attached to the page document "+ e.getStackTrace());
            UtilsMethods.takeScreenshot(driver);
        } catch (NoSuchElementException e) {
            logger.warn("Element was not found in DOM "+ e.getStackTrace());
            UtilsMethods.takeScreenshot(driver);
        } catch (Exception e) {
            logger.warn("Unable to click on element "+ e.getStackTrace());
            UtilsMethods.takeScreenshot(driver);
        }
    }

    public List<WebElement> getAddAccounts() {
        return addAccounts;
    }

    public List<WebElement> getChooseAccounts() {
        return chooseAccounts;
    }
}
