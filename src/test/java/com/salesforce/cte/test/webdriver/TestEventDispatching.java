/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.cte.test.webdriver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.salesforce.cte.admin.TestAdvisorAdministrator;
import com.salesforce.cte.listener.selenium.EventDispatcher;
import com.salesforce.cte.listener.selenium.FullLogger;
import com.salesforce.cte.listener.selenium.IEventListener;
import com.salesforce.cte.listener.selenium.ScreenshotLogger;
import com.salesforce.cte.listener.testng.TestListener;

/**
 * @author gneumann
 *
 */
@Listeners(TestListener.class)
public class TestEventDispatching {
	private static MockRemoteWebDriver wd;
	private static FullLogger fullLogger;
	private static ScreenshotLogger screenshotLogger;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MutableCapabilities mcap = new MutableCapabilities();
		mcap.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, "true");
		MockCommandExecutor mce = new MockCommandExecutor();
		wd = new MockRemoteWebDriver(mce, mcap);
		mce.setRemoteWebDriver(wd);
		List<IEventListener> eventListeners = EventDispatcher.getInstance().getImmutableListOfEventListeners();
		for (IEventListener listener : eventListeners) {
			if (listener instanceof FullLogger) {
				fullLogger = (FullLogger) listener;
			}
			if (listener instanceof ScreenshotLogger){
				screenshotLogger = (ScreenshotLogger) listener;
			}
		}
		System.setProperty("testadvisor.capturescreenshot", "true");
	}

//	@Test(priority = 1)
	public void testOnExceptionWithNoCurrentEventSet() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		EventDispatcher.getInstance().onException(null, null);
		assertNumOfLogEntries("click", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 0);
		assertNumOfLogEntries("click", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	// Test ScreeshotLogger
	@Test(priority = 2)
	public void testClick() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.click();
		assertNumOfLogEntries("click", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("click", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);

		TestAdvisorAdministrator administrator = TestAdvisorAdministrator.getInstance();
		assertEquals(administrator.getTestCaseExecution().eventList.size(), 1);
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getEventLevel(), Level.INFO.toString());
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getSeleniumCmd(), "webElement.click");
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getSeleniumLocator(), "By.id(\"someId\")");
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getEventSource(), "com.salesforce.cte.listener.selenium.AbstractEventListener");
		assertTrue(administrator.getTestCaseExecution().eventList.get(0).getScreenshotPath().contains("screenshot"));
		assertTrue(administrator.getTestCaseExecution().eventList.get(0).getScreenshotRecordNumber()>=0);	
	}
	
	@Test(priority = 2)
	public void testClickByChildElement() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		WebElement childWe = we.findElement(By.id("someOtherId"));
		assertNotNull(childWe);
		childWe.click();
		assertNumOfLogEntries("clickByChildElement", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 6);
	}

	@Test(priority = 2)
	public void testGet() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.get("https://www.salesforce.com");
		assertNumOfLogEntries("get", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("sendkeys", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testExecuteScriptWithScreenshot() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.executeScript("click");
		assertNumOfLogEntries("click", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("click", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testExecuteScriptWithoutScreenshot() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		String response = (String) wd.executeScript("style.border='3px solid'");
		assertEquals(response, "highlighted web element");
		assertNumOfLogEntries("script", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertEquals(numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size());
	}

	@Test(priority = 2)
	public void testSubmit() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.submit();
		assertNumOfLogEntries("submit", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("submit", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testSendKeysByElement() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.sendKeys("abc");
		assertNumOfLogEntries("sendkeys", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("sendkeys", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testClear() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.clear();
		assertNumOfLogEntries("clear", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("clear", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testGetTagName() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getTagName(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNumOfLogEntries("getTagName", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getTagName", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetDomProperty() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getDomProperty("someDomProperty"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNumOfLogEntries("getDomProperty", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getDomProperty", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetDomAttribute() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getDomAttribute("someDomAttribute"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNumOfLogEntries("getDomAttribute", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getDomAttribute", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetAriaRole() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getAriaRole(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNumOfLogEntries("getAriaRole", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getAriaRole", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetAccessibleName() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getAccessibleName(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNumOfLogEntries("getAccessibleName", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getAccessibleName", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testIsSelected() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we.isSelected());
		assertNumOfLogEntries("isSelected", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("isSelected", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testIsSelectedWithIncorrectReturnType() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		boolean wasExceptionThrown = false;
		try {
			we.isSelected();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertNumOfLogEntries("isSelected", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 3);
		assertNumOfLogEntries("isSelected", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testIsEnabled() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we.isEnabled());
		assertNumOfLogEntries("isEnabled", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("isEnabled", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testIsEnabledWithIncorrectReturnType() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		boolean wasExceptionThrown = false;
		try {
			we.isEnabled();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertNumOfLogEntries("isEnabled", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 3);
		assertNumOfLogEntries("isEnabled", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testIsDisplayed() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we.isDisplayed());
		assertNumOfLogEntries("isDisplayed", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("isDisplayed", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testIsDisplayedWithIncorrectReturnType() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		boolean wasExceptionThrown = false;
		try {
			we.isDisplayed();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertNumOfLogEntries("isDisplayed", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 3);
		assertNumOfLogEntries("isDisplayed", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetCssValue() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getCssValue("someCssValue"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNumOfLogEntries("getCssValue", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getCssValue", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetLocation() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Point point = we.getLocation();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertNumOfLogEntries("getLocation", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getLocation", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetSize() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Dimension size = we.getSize();
		Assert.assertEquals(size.getWidth(), 0);
		Assert.assertEquals(size.getHeight(), 0);
		assertNumOfLogEntries("getSize", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getSize", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetRect() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Rectangle rect = we.getRect();
		Assert.assertEquals(rect.getX(), 0);
		Assert.assertEquals(rect.getY(), 0);
		Assert.assertEquals(rect.getWidth(), 0);
		Assert.assertEquals(rect.getHeight(), 0);
		assertNumOfLogEntries("getRect", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getRect", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesInViewPort() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		RemoteWebElement rwe = (RemoteWebElement) we;
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		Point point = coordinates.inViewPort();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertNumOfLogEntries("getCoordinates", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getCoordinates", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesOnPage() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		RemoteWebElement rwe = (RemoteWebElement) we;
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		Point point = coordinates.onPage();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertNumOfLogEntries("getCoordinates", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getCoordinates", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesOnScreen() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		RemoteWebElement rwe = (RemoteWebElement) we;
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		boolean wasExceptionThrown = false;
		try {
			coordinates.onScreen();
		} catch (UnsupportedOperationException uoe) {
			wasExceptionThrown = true;
		}
		assertTrue(wasExceptionThrown, "UnsupportedOperationException not thrown as expected");
		assertNumOfLogEntries("getCoordinates", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getCoordinates", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesGetAuxiliary() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		RemoteWebElement rwe = (RemoteWebElement) we;
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		String id = (String) coordinates.getAuxiliary();
		assertTrue(id.startsWith(MockCommandExecutor.ELEMENT_ID_PREFIX));
		assertNumOfLogEntries("getCoordinates", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getCoordinates", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetShadowRoot() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		SearchContext sc = we.getShadowRoot();
		Assert.assertNotNull(sc);
		Assert.assertTrue(sc == wd);
		assertNumOfLogEntries("getShadowRoot", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("getShadowRoot", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testSendKeysByElementSendingCharsOneByOne() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someOtherId"));
		assertNotNull(we);
		we.sendKeys("a");
		we.sendKeys("b");
		we.sendKeys("c");
		assertNumOfLogEntries("sendkeys", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 8);
		assertNumOfLogEntries("sendkeys", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testClose() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.close();
		assertNumOfLogEntries("close", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("close", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testTo() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.navigate().to("http://somewhere");
		assertNumOfLogEntries("NavigateTo", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("NavigateTo", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testToURL() throws MalformedURLException {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.navigate().to(new URL("http://somewhere"));
		assertNumOfLogEntries("NavigateToURL", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("NavigateToURL", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testBack() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.navigate().back();;
		assertNumOfLogEntries("NavigateBack", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("NavigateBack", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testForward() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.navigate().forward();;
		assertNumOfLogEntries("NavigateForward", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("NavigateForward", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testDismiss() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.switchTo().alert().dismiss();
		assertNumOfLogEntries("Dismiss", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("Dismiss", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testAccept() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.switchTo().alert().accept();
		assertNumOfLogEntries("Accept", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("Accept", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testSendKeysToAlert() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.switchTo().alert().sendKeys("some");
		assertNumOfLogEntries("SendKeys", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("SendKeys", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	// Test FullLogger
	@Test(priority = 2)
	public void testFindElementByWebDriver() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		assertNumOfLogEntries("findElementByWebDriver", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("findElementByWebDriver", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}
	
	@Test(priority = 2)
	public void testFindElementByElement() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		WebElement childWe = we.findElement(By.id("someOtherId"));
		assertNotNull(childWe);
		assertNumOfLogEntries("findElementByElement", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 4);
		assertNumOfLogEntries("findElementByElement", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetTitle() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		assertEquals(MockCommandExecutor.STATE_OK, wd.getTitle());
		assertNumOfLogEntries("getTitle", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("getTitle", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testExecuteAsyncScript() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.executeAsyncScript("some script");
		assertNumOfLogEntries("executeAsyncScript", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("executeAsyncScript", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testSetSize() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.manage().window().setSize(new Dimension(1024, 768));
		assertNumOfLogEntries("setSize", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("setSize", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testSetPosition() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.manage().window().setPosition(new Point(0, 0));
		assertNumOfLogEntries("setPosition", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("setPosition", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testRefresh() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.navigate().refresh();
		assertNumOfLogEntries("refresh", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("refresh", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testGetURL() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.getCurrentUrl();
		assertNumOfLogEntries("GetURL", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testScreenshot() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.getScreenshotAs(OutputType.FILE);
		assertNumOfLogEntries("Screenshot", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testGetPageSource() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.getPageSource();
		assertNumOfLogEntries("GetPageSource", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testWindowHandle() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.getWindowHandle();
		assertNumOfLogEntries("GetWindowHandle", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testDeleteAllCookies() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().deleteAllCookies();
		assertNumOfLogEntries("deleteAllCookies", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testDeleteCookieNamed() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().deleteCookieNamed("name");;
		assertNumOfLogEntries("deleteCookieNamed", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	} 

	@Test(priority = 2)
	public void testAddCookie() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().addCookie(new Cookie("name", "value"));
		assertNumOfLogEntries("addCookie", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testGetCookies() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().getCookies();
		assertNumOfLogEntries("getCookies", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testGetCookie() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().getCookieNamed("name");
		assertNumOfLogEntries("GetCookie", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testDeleteCookies() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().deleteCookieNamed("name");
		assertNumOfLogEntries("DeleteCookie", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testSetTimeout() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
		assertNumOfLogEntries("setScriptTimeout", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testImplicitlyWait() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		assertNumOfLogEntries("implicitlyWait", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testPageLoadTimeout() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
		assertNumOfLogEntries("pageLoadTimeout", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testTakeScreenshots() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		wd.getScreenshotAs(OutputType.FILE);
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.getScreenshotAs(OutputType.FILE);
		assertNumOfLogEntries("takeScreenshots", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 6);
		assertNumOfLogEntries("refresh", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testQuit() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.quit();
		assertNumOfLogEntries("quit", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	@Test(priority = 2)
	public void testWebDriverExceptionHandling() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		boolean wasExceptionThrown = false;
		MockCommandExecutor.setDoTriggerWebDriverException();
		// this command will not get executed due to a forced exception
		try {
			wd.get("https://www.salesforce.com");
		} catch (WebDriverException we) {
			wasExceptionThrown = true;
		}
		assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertNumOfLogEntries("get", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("get", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	/*
	 * RemoteAlert specific tests
	 */
	@Test(priority = 2)
	public void testGetAlertText() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		Alert alert = wd.switchTo().alert();
		assertNotNull(alert);
		assertEquals(alert.getText(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNumOfLogEntries("Alert.getText", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("Alert.getText", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 0);
	}

	@Test(priority = 2)
	public void testDismissAlert() {
		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		int numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
		Alert alert = wd.switchTo().alert();
		assertNotNull(alert);
		alert.dismiss();
		assertNumOfLogEntries("Alert.dismiss", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
		assertNumOfLogEntries("Alert.dismiss", numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), 1);
	}

	@Test(priority = 2)
	public void testSecondWebDriver(){
		wd.quit();
		MutableCapabilities mcap = new MutableCapabilities();
		mcap.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, "true");
		MockCommandExecutor mce = new MockCommandExecutor();
		wd = new MockRemoteWebDriver(mce, mcap);
		mce.setRemoteWebDriver(wd);

		int numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		wd.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
		assertNumOfLogEntries("setScriptTimeout", numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), 2);
	}

	private void assertNumOfLogEntries(String command, int before, int after, int expectedDifference) {
		System.out.println(String.format("Number of events logged before %s(): %d, and after: %d", command, before, after));
		assertTrue(after >= before);
		assertEquals(after - before , expectedDifference);
	}
}
