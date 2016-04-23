package com.epam.cdpSelenium.UITests;

import com.epam.cdpSelenium.businessObject.users.UserCreator;
import com.epam.cdpSelenium.core.webdriver.factory.ChromeDriverSingleton;
import com.epam.cdpSelenium.core.webdriver.factory.FirefoxDriverSingleton;
import com.epam.cdpSelenium.ui.pages.*;
import com.epam.cdpSelenium.businessObject.users.User;
import com.epam.cdpSelenium.utils.UtilsMethods;
import com.epam.cdpSelenium.utils.WebDriverDecorator;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import sun.rmi.runtime.Log;

import java.util.concurrent.TimeUnit;

public class MarkAsSpamTest {
    protected WebDriver driver;
    Logger logger = LogManager.getRootLogger();

    @Before
    public void setupDecoratedDriver() {
        driver = FirefoxDriverSingleton.getDecoratedWebDriverInstance();
        //driver = ChromeDriverSingleton.getDecoratedWebDriverInstance();
        logger.info("WebDriver is created");
    }

    @Test
    public void MarkAsSpamTestRun(){
        UtilsMethods wait = new UtilsMethods(driver);
        User user1 = UserCreator.createUser1();
        logger.info("User " + user1.getUsername() + " is created");
        User user2 = UserCreator.createUserWithCredentials("gmbdaily003", "gmbdaily003mially");
        logger.info("User " + user2.getUsername() + " is created");
        InboxPage inboxPage;

        String messageTitle01, messageTitle02;
        boolean isMessageInSpam = false;

        //send message from user1 to user2
        inboxPage = user1.loginToGmail(driver);
        logger.info("User " + user1.getUsername() + " is logged in Gmail");
        UtilsMethods.takeScreenshot(driver);
        messageTitle01 = inboxPage.createMessageTo(driver, user2); //save unique message title to find message later
        inboxPage.logout(driver);

        //find message in Inbox and mark as spam
        inboxPage = user2.loginToGmail(driver);
        logger.info("User " + user2.getUsername() + " is logged in Gmail");
        inboxPage.searchForMessageInInboxAndMarkAsSpam(driver, messageTitle01);
        logger.info("Test message is found in Inbox and is marked as Spam");
        inboxPage.logout(driver);

        //send message from user1 to user2
        inboxPage = user1.loginToGmail(driver);
        logger.info("User " + user1.getUsername() + " is logged in Gmail");
        messageTitle02 = inboxPage.createMessageTo(driver, user2);
        inboxPage.logout(driver);

        //find message in Spam and un-spam it (revert the state)
        inboxPage = user2.loginToGmail(driver);
        logger.info("User " + user2.getUsername() + " is logged in Gmail");
        isMessageInSpam = inboxPage.searchForMessageInSpamAndUnspam(driver, messageTitle02);
        logger.info("Test message is found in Spam folder and is unmarked as Spam");
        inboxPage.logout(driver);

        driver.quit();
        Assert.assertTrue(isMessageInSpam);
    }

    @After
    public void tearDown(){
        FirefoxDriverSingleton.closeDriver();
        //ChromeDriverSingleton.closeDriver();
    }

}


