package com.epam.cdpSelenium.ui.pages;

import com.epam.cdpSelenium.utils.UtilsMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SpamPage {
    private WebDriver driver;
    Logger logger = LogManager.getRootLogger();

    @FindBy(xpath = "//tbody/tr/td[6]")
    private List<WebElement> emailTitles;

    public SpamPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public Boolean searchForMessageAndUnspam(String title) {
        boolean isFound = false;
        for (WebElement element : emailTitles) {
            if (element.isDisplayed() && element.getText().contains(title)) {
                logger.info("Message '" + title + "' is found in Spam");
                isFound = true;
                element.click();
                UtilsMethods.getWait().until(ExpectedConditions.titleContains(title));
                MessagePage messagePage = new MessagePage(driver);
                messagePage.unSpamLetter();
                logger.info("Message '" + title + "' is unmarked as Spam");
            }
        }
        if (!isFound) {
            logger.fatal("No message in Spam. Message might not get to the Spam due to account history (trusted account).");
            UtilsMethods.takeScreenshot(driver);
        }
        return isFound;
    }

}
