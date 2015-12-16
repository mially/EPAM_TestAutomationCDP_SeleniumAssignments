package com.epam.cdpSelenium.ui.pages;

import com.epam.cdpSelenium.utils.UtilsMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MessagePage {
    private WebDriver driver;
    Logger logger = LogManager.getRootLogger();

    @FindBy(xpath = "//div[@id=':5']/div[@gh='tm']//div[@act='9']")
    private List<WebElement> markAsSpamButtons;

    @FindBy(xpath = "//div[@id=':5']/div[@gh='tm']//div[@act='18']")
    private List<WebElement> unmarkAsSpamButtons;

    @FindBy(xpath = "//button[contains(text(), 'just spam')]")
    private WebElement justSpamButton;

    public MessagePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void markLetterAsSpam(WebDriver driver) {
        try {
            UtilsMethods.getWait().until(ExpectedConditions.alertIsPresent());
            UtilsMethods.takeScreenshot(driver); //just4test
            Alert alert = driver.switchTo().alert();
            logger.info("Alert is fired");
            UtilsMethods.takeScreenshot(driver);
            alert.accept();
        } catch (Exception e) {
            logger.warn("No alert this time");
        }
        if (!markAsSpamButtons.isEmpty()) {
            String mainWindowHandle = driver.getWindowHandle();
            markAsSpamButtons.get(0).click(); //click "report spam' button
            Set s = driver.getWindowHandles(); //manage pop-up if present
            Iterator iter = s.iterator();
            while (iter.hasNext()) {
                String popupHandle=iter.next().toString();
                if(!popupHandle.contains(mainWindowHandle))
                {
                    driver.switchTo().window(popupHandle);
                    UtilsMethods.takeScreenshot(driver, "MarkAsSpamPopUP");
                    getJustSpamButton().click(); //Click pop-up button
                }
            }
            logger.info("Message is marked as spam");
        } else {
            logger.warn("Can't mark this message as spam!");
            UtilsMethods.takeScreenshot(driver);
        }
    }

    public void unSpamLetter(){
        if (!unmarkAsSpamButtons.isEmpty()) {
            unmarkAsSpamButtons.get(0).click();
            logger.info("Message is un-marked as Spam");
        } else {
            logger.warn("No unspam element on the page");
            UtilsMethods.takeScreenshot(driver);
        }
    }
    public WebElement getJustSpamButton() {
        return justSpamButton;
    }
}
