package com.salesforce.cte.test.webdriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.salesforce.cte.admin.TestAdvisorAdministrator;
import com.salesforce.cte.listener.testng.TestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

@Listeners(TestListener.class)
public class WebDriverIntegrationTest {
    
    @Test
    public void testChromeWebDriver() throws IOException{
        try(ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os)){
            System.setOut(ps);
            System.setErr(ps);

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
            String traceId = TestAdvisorAdministrator.getInstance().getTestCaseExecution().getTraceId();

            Pattern pattern = Pattern.compile("INFO: Set trace id as (\\w{16})");
            Matcher matcher = pattern.matcher(os.toString());
            assertTrue(matcher.find());
            assertEquals("INFO: Set trace id as " + traceId, matcher.group(0));
        }
    }

    @Test
    public void testChromeDriverService() throws IOException{
        try(ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os)){
            System.setOut(ps);
            System.setErr(ps);

            ChromeDriverService service;

            service = new ChromeDriverService.Builder()       
                .usingDriverExecutable(new File("/Users/ytao/source/TestAdvisor-Lib-Selenium-4/lib/chromedriver"))         
                .usingAnyFreePort()         
                .build();     

            service.start();   

            ChromeOptions chromeOptions = new ChromeOptions();
            WebDriver driver = RemoteWebDriver.builder().address(service.getUrl()).oneOf(chromeOptions).build();

            driver.get("https://test.salesforce.com/");  
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                                                    .withTimeout(Duration.ofSeconds(30))
                                                    .pollingEvery(Duration.ofMillis(500));      
            fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Login")));

            driver.get("https://login.salesforce.com/");    
            fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Login")));

            driver.quit();

            Pattern pattern = Pattern.compile("INFO: Set trace id as (\\w{16})");
            Matcher matcher = pattern.matcher(os.toString());
            assertFalse(matcher.find());
        }
    }

    @Test
    public void testBrowserStack() throws IOException {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os)){
            System.setOut(ps);
            System.setErr(ps);

            String remoteUrl = "https://danstead_L51Mp9:ReCUgTG8Qf2srkgV1Ygs@hub-cloud.browserstack.com/wd/hub";

            //ChromeOptions browserOptions = new ChromeOptions();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
            browserstackOptions.put("os", "OS X");
            browserstackOptions.put("osVersion", "Mojave");
            browserstackOptions.put("sessionName", "Network Interception Test");
            browserstackOptions.put("seleniumVersion", "4.0.0");
            browserstackOptions.put("seleniumCdp", true);
            capabilities.setCapability("bstack:options", browserstackOptions);

            WebDriver driver = new RemoteWebDriver(new URL(remoteUrl), capabilities);
            
            driver.get("https://test.salesforce.com/");  
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                                                    .withTimeout(Duration.ofSeconds(30))
                                                    .pollingEvery(Duration.ofMillis(500));      
            fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Login")));

            driver.get("https://login.salesforce.com/");    
            fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Login")));

            driver.quit();
            
            String traceId = TestAdvisorAdministrator.getInstance().getTestCaseExecution().getTraceId();

            Pattern pattern = Pattern.compile("INFO: Set trace id as (\\w{16})");
            Matcher matcher = pattern.matcher(os.toString());
            assertTrue(matcher.find());
            assertEquals("INFO: Set trace id as " + traceId, matcher.group(0));
        }
    }
}
