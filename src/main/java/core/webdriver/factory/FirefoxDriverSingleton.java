package com.epam.cdpSelenium.core.webdriver.factory;

import com.epam.cdpSelenium.utils.WebDriverDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class FirefoxDriverSingleton {
    private static WebDriver driver;

    private FirefoxDriverSingleton() {}

    public static WebDriver getDecoratedWebDriverInstance() {
        if (null == driver) {
            driver = new FirefoxDriver();
           // driver = new WebDriverDecorator(driver);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
        return driver;
    }

    public static void closeDriver(){
        driver.quit();
        driver = null;
    }
}
