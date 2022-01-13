package com.salesforce.cte.test.webdriver;

import static org.testng.Assert.assertTrue;

import java.time.Duration;

import com.salesforce.cte.listener.testng.TestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

@Listeners(TestListener.class)
public class WebDriverIntegrationTest {
    
    @Test
    public void testChromeWebDriver(){
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(); 
        driver.get("https://test.salesforce.com/");  
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                                                .withTimeout(Duration.ofSeconds(30))
                                                .pollingEvery(Duration.ofMillis(500));      
        fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Login")));

        driver.get("https://login.salesforce.com/");    
        fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Login")));

        driver.quit();
        assertTrue(true);
    }
}
