package com.epam.cdpSelenium.core.webdriver.factory;

import com.epam.cdpSelenium.utils.WebDriverDecorator;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class ChromeDriverSingleton {
    private static WebDriver driver;

    private ChromeDriverSingleton() {}

    public static WebDriver getDecoratedWebDriverInstance() {
        if (null == driver) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver = new WebDriverDecorator(driver);
        }
        return driver;
    }

    public static void closeDriver(){
        driver.quit();
        driver = null;
    }
}
