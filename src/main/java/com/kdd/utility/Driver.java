package com.kdd.utility;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class Driver {


    public static WebDriver driver;

    public Driver() {

    }

    public static WebDriver getDriver() {


        //System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\main\\java\\resources\\chromedriver.exe");
       driver = new FirefoxDriver();

//driver=  WebDriverManager.chromedriver().setup();







  // driver = new ChromeDriver(new ChromeOptions().setHeadless(true));

       // driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));


        return driver;
    } //

    public static void closeDriver() {
        if (driver != null) {
            driver.close();
            driver = null;
        }
    }//

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }//

}//
