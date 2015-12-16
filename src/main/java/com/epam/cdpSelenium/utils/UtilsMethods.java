package com.epam.cdpSelenium.utils;

import com.epam.cdpSelenium.core.webdriver.factory.FirefoxDriverSingleton;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtilsMethods {

    private static WebDriverWait wait;
    static Logger logger = LogManager.getRootLogger();

    public UtilsMethods(WebDriver driver){
        this.wait = new WebDriverWait(driver, 10);
    }
    public static WebDriverWait getWait(){
        return wait;
    }

    public static void clear(WebElement element){
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
    }

    //takes screenshot with custom message in file name
    public static void takeScreenshot(WebDriver driver, String message){
        //get method name for screenshot naming
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement t = stacktrace[2];
        //if default takeScreenshot method was used
        if (message.isEmpty()){
            t = stacktrace[3];
        }
        String methodName = t.getMethodName();

        //Cast WebDriver and get a screenshot
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        //File location and naming using Date + Active Method
        String screenshotName = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date())
                + "[" + methodName + "]" + message + ".png";
        String workingDirectory = System.getProperty("user.dir") + "/target/screenshots/";

        try {
            FileUtils.copyFile(screenshotFile, new File(workingDirectory + screenshotName));
            logger.info("Screenshot " + screenshotName + " is saved");
        } catch (IOException e) {
            logger.error("Screenshot can't be saved, " + e.getMessage());
        }
    }

    //takes screenshot with default name
    public static void takeScreenshot(WebDriver driver) {
        String message = "";
        takeScreenshot(driver, message);
    }

    public static void highlightElement(WebDriver driver, WebElement elementToHighlight){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String background = elementToHighlight.getCssValue("backgroundColor");

        jsExecutor.executeScript("arguments[0].style.backgroundColor = '" + "yellow" +  "'", elementToHighlight);
        takeScreenshot(driver, "ElementIsHighlighted");
        jsExecutor.executeScript("arguments[0].style.backgroundColor = '" + background +  "'", elementToHighlight);
    }
}
