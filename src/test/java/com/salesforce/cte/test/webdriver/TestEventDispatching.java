/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.cte.test.webdriver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Set;
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
	
	private static int numOfEventsBefore;
	private static int numOfScreenshotEventsBefore;

	@BeforeClass
	public static void setUpBeforeClass() {
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
		setEventCounters();
		EventDispatcher.getInstance().onException(null, null);
		assertEventCounters("click", 0, 0);
	}

	// Test ScreenshotLogger
	@Test(priority = 2)
	public void testClick() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.click();
		assertEventCounters("click", 4, 1);

		TestAdvisorAdministrator administrator = TestAdvisorAdministrator.getInstance();
		assertEquals(administrator.getTestCaseExecution().eventList.size(), 1);
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getEventLevel(), Level.INFO.toString());
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getSeleniumCmd(), "webElement.click");
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getSeleniumLocator(), "By.id(\"someId\")");
		assertEquals(administrator.getTestCaseExecution().eventList.get(0).getEventSource(), "com.salesforce.cte.listener.selenium.AbstractEventListener");
		Assert.assertTrue(administrator.getTestCaseExecution().eventList.get(0).getScreenshotPath().contains("screenshot"));
		Assert.assertTrue(administrator.getTestCaseExecution().eventList.get(0).getScreenshotRecordNumber()>=0);
	}

	@Test(priority = 2)
	public void testClickByChildElement() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		WebElement childWe = we.findElement(By.id("someOtherId"));
		assertNotNull(childWe);
		childWe.click();
		assertEventCounters("clickByChildElement", 6, 1);
	}

	@Test(priority = 2)
	public void testGet() {
		setEventCounters();
		wd.get("https://www.salesforce.com");
		assertEventCounters("get", 2, 1);
	}

	@Test(priority = 2)
	public void testExecuteScriptWithScreenshot() {
		setEventCounters();
		wd.executeScript("click");
		assertEventCounters("executeScript", 2, 1);
	}

	@Test(priority = 2)
	public void testExecuteScriptWithoutScreenshot() {
		setEventCounters();
		String response = (String) wd.executeScript("style.border='3px solid'");
		assertEquals(response, "highlighted web element");
		assertEventCounters("executeScript", 2, 0);
	}

	@Test(priority = 2)
	public void testSubmit() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.submit();
		assertEventCounters("submit", 4, 1);
	}

	@Test(priority = 2)
	public void testSendKeysByElement() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.sendKeys("abc");
		assertEventCounters("sendKeys", 4, 1);
	}

	@Test(priority = 2)
	public void testClear() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.clear();
		assertEventCounters("clear", 4, 1);
	}

	@Test(priority = 2)
	public void testGetTagName() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getTagName(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getTagName", 4, 0);
	}

	@Test(priority = 2)
	public void testGetDomProperty() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getDomProperty("someDomProperty"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getDomProperty", 4, 0);
	}

	@Test(priority = 2)
	public void testGetDomAttribute() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getDomAttribute("someDomAttribute"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getDomAttribute", 4, 0);
	}

	@Test(priority = 2)
	public void testGetAriaRole() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getAriaRole(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getAriaRole", 4, 0);
	}

	@Test(priority = 2)
	public void testGetAccessibleName() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getAccessibleName(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getAccessibleName", 4, 0);
	}

	@Test(priority = 2)
	public void testIsSelected() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we.isSelected());
		assertEventCounters("isSelected", 4, 0);
	}

	@Test(priority = 2)
	public void testIsSelectedWithIncorrectReturnType() {
		setEventCounters();
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		boolean wasExceptionThrown = false;
		try {
			we.isSelected();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertEventCounters("isSelected", 3, 0);
	}

	@Test(priority = 2)
	public void testIsEnabled() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we.isEnabled());
		assertEventCounters("isEnabled", 4, 0);
	}

	@Test(priority = 2)
	public void testIsEnabledWithIncorrectReturnType() {
		setEventCounters();
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		boolean wasExceptionThrown = false;
		try {
			we.isEnabled();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertEventCounters("isEnabled", 3, 0);
	}

	@Test(priority = 2)
	public void testIsDisplayed() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we.isDisplayed());
		assertEventCounters("isDisplayed", 4, 0);
	}

	@Test(priority = 2)
	public void testIsDisplayedWithIncorrectReturnType() {
		setEventCounters();
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		boolean wasExceptionThrown = false;
		try {
			we.isDisplayed();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertEventCounters("isDisplayed", 3, 0);
	}

	@Test(priority = 2)
	public void testGetCssValue() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertEquals(we.getCssValue("someCssValue"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getCssValue", 4, 0);
	}

	@Test(priority = 2)
	public void testGetLocation() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Point point = we.getLocation();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertEventCounters("getLocation", 4, 0);
	}

	@Test(priority = 2)
	public void testGetSize() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Dimension size = we.getSize();
		Assert.assertEquals(size.getWidth(), 0);
		Assert.assertEquals(size.getHeight(), 0);
		assertEventCounters("getSize", 4, 0);
	}

	@Test(priority = 2)
	public void testGetRect() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Rectangle rect = we.getRect();
		Assert.assertEquals(rect.getX(), 0);
		Assert.assertEquals(rect.getY(), 0);
		Assert.assertEquals(rect.getWidth(), 0);
		Assert.assertEquals(rect.getHeight(), 0);
		assertEventCounters("getRect", 4, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesInViewPort() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		Point point = coordinates.inViewPort();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertEventCounters("getCoordinatesInViewPort", 4, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesOnPage() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		Point point = coordinates.onPage();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertEventCounters("getCoordinatesOnPage", 4, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesOnScreen() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		boolean wasExceptionThrown = false;
		try {
			coordinates.onScreen();
		} catch (UnsupportedOperationException uoe) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "UnsupportedOperationException not thrown as expected");
		assertEventCounters("getCoordinatesOnScreen", 4, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesGetAuxiliary() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		String id = (String) coordinates.getAuxiliary();
		Assert.assertTrue(id.startsWith(MockCommandExecutor.ELEMENT_ID_PREFIX));
		assertEventCounters("getCoordinatesGetAuxiliary", 4, 0);
	}

	@Test(priority = 2)
	public void testGetShadowRoot() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		SearchContext sc = we.getShadowRoot();
		Assert.assertNotNull(sc);
		Assert.assertSame(sc, wd);
		assertEventCounters("getShadowRoot", 4, 0);
	}

	@Test(priority = 2)
	public void testSendKeysByElementSendingCharsOneByOne() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someOtherId"));
		assertNotNull(we);
		we.sendKeys("a");
		we.sendKeys("b");
		we.sendKeys("c");
		assertEventCounters("sendKeys", 8, 1);
	}

	@Test(priority = 2)
	public void testClose() {
		setEventCounters();
		wd.close();
		assertEventCounters("close", 2, 1);
	}

	@Test(priority = 2)
	public void testTo() {
		setEventCounters();
		wd.navigate().to("http://somewhere");
		assertEventCounters("to", 2, 1);
	}

	@Test(priority = 2)
	public void testToURL() throws MalformedURLException {
		setEventCounters();
		wd.navigate().to(new URL("http://somewhere"));
		assertEventCounters("toURL", 2, 1);
	}

	@Test(priority = 2)
	public void testBack() {
		setEventCounters();
		wd.navigate().back();
		assertEventCounters("back", 2, 1);
	}

	@Test(priority = 2)
	public void testForward() {
		setEventCounters();
		wd.navigate().forward();
		assertEventCounters("forward", 2, 1);
	}

	@Test(priority = 2)
	public void testDismiss() {
		setEventCounters();
		wd.switchTo().alert().dismiss();
		assertEventCounters("dismiss", 2, 1);
	}

	@Test(priority = 2)
	public void testAccept() {
		setEventCounters();
		wd.switchTo().alert().accept();
		assertEventCounters("accept", 2, 1);
	}

	@Test(priority = 2)
	public void testSendKeysToAlert() {
		setEventCounters();
		wd.switchTo().alert().sendKeys("some");
		assertEventCounters("sendKeysToAlert", 2, 1);
	}

	// Test FullLogger
	@Test(priority = 2)
	public void testFindElementByWebDriver() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		assertEventCounters("findElementByWebDriver", 2, 0);
	}
	
	@Test(priority = 2)
	public void testFindElementByElement() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		WebElement childWe = we.findElement(By.id("someOtherId"));
		assertNotNull(childWe);
		assertEventCounters("findElementByElement", 4, 0);
	}

	@Test(priority = 2)
	public void testGetTitle() {
		setEventCounters();
		assertEquals(MockCommandExecutor.STATE_OK, wd.getTitle());
		assertEventCounters("getTitle", 2, 0);
	}

	@Test(priority = 2)
	public void testExecuteAsyncScript() {
		setEventCounters();
		wd.executeAsyncScript("some script");
		assertEventCounters("executeAsyncScript", 2, 0);
	}

	@Test(priority = 2)
	public void testSetSize() {
		setEventCounters();
		wd.manage().window().setSize(new Dimension(1024, 768));
		assertEventCounters("setSize", 2, 0);
	}

	@Test(priority = 2)
	public void testSetPosition() {
		setEventCounters();
		wd.manage().window().setPosition(new Point(0, 0));
		assertEventCounters("setPosition", 2, 0);
	}

	@Test(priority = 2)
	public void testRefresh() {
		setEventCounters();
		wd.navigate().refresh();
		assertEventCounters("refresh", 2, 0);
	}

	@Test(priority = 2)
	public void testActiveElement() {
		setEventCounters();
		WebElement elem = wd.switchTo().activeElement();
		assertNotNull(elem);
		assertEventCounters("activeElement", 2, 0);
	}

	@Test(priority = 2)
	public void testDefaultContent() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().defaultContent();
		assertNotNull(returnedWd);
		assertEventCounters("defaultContent", 2, 0);
	}

	@Test(priority = 2)
	public void testFrameByIndex() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().frame(0);
		assertNotNull(returnedWd);
		assertEventCounters("frameByIndex", 2, 0);
	}

	@Test(priority = 2)
	public void testFrameByName() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().frame(MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNotNull(returnedWd);
		assertEventCounters("frameByName", 2, 0);
	}

	@Test(priority = 2)
	public void testFrameByElement() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().frame(wd.findElement(By.id("someId")));
		assertNotNull(returnedWd);
		assertEventCounters("frameByElement", 4, 0);
	}

	@Test(priority = 2)
	public void testParentFrame() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().parentFrame();
		assertNotNull(returnedWd);
		assertEventCounters("parentFrame", 2, 0);
	}

	@Test(priority = 2)
	public void testWindow() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().window(MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNotNull(returnedWd);
		assertEventCounters("window", 2, 0);
	}

	@Test(priority = 2)
	public void testNewWindow() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().newWindow(WindowType.WINDOW);
		assertNotNull(returnedWd);
		assertEventCounters("newWindow", 2, 0);
	}

	@Test(priority = 2)
	public void testGetURL() {
		setEventCounters();
		wd.getCurrentUrl();
		assertEventCounters("getURL", 2, 0);
	}

	@Test(priority = 2)
	public void testScreenshot() {
		setEventCounters();
		wd.getScreenshotAs(OutputType.FILE);
		assertEventCounters("screenshot", 2, 0);
	}

	@Test(priority = 2)
	public void testGetPageSource() {
		setEventCounters();
		String source = wd.getPageSource();
		assertNotNull(source);
		Assert.assertEquals(source, MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getPageSource", 2, 0);
	}

	@Test(priority = 2)
	public void testWindowHandle() {
		setEventCounters();
		String handle = wd.getWindowHandle();
		assertNotNull(handle);
		Assert.assertEquals(handle, MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getWindowHandle", 2, 0);
	}

	@Test(priority = 2)
	public void testDeleteAllCookies() {
		setEventCounters();
		wd.manage().deleteAllCookies();
		assertEventCounters("deleteAllCookies", 2, 0);
	}

	@Test(priority = 2)
	public void testDeleteCookieNamed() {
		setEventCounters();
		wd.manage().deleteCookieNamed("name");
		assertEventCounters("deleteCookieNamed", 2, 0);
	}

	@Test(priority = 2)
	public void testAddCookie() {
		setEventCounters();
		wd.manage().addCookie(new Cookie("name", "value"));
		assertEventCounters("addCookie", 2, 0);
	}

	@Test(priority = 2)
	public void testGetCookies() {
		setEventCounters();
		Set<Cookie> cookies = wd.manage().getCookies();
		assertNotNull(cookies);
		assertEventCounters("getCookies", 2, 0);
	}

	@Test(priority = 2)
	public void testGetCookie() {
		setEventCounters();
		wd.manage().getCookieNamed("name");
		assertEventCounters("getCookie", 2, 0);
	}

	@Test(priority = 2)
	public void testDeleteCookies() {
		setEventCounters();
		wd.manage().deleteCookieNamed("name");
		assertEventCounters("deleteCookie", 2, 0);
	}

	@Test(priority = 2)
	public void testSetTimeout() {
		setEventCounters();
		wd.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
		assertEventCounters("setScriptTimeout", 2, 0);
	}

	@Test(priority = 2)
	public void testGetScriptTimeout() {
		setEventCounters();
		Duration timeout = wd.manage().timeouts().getScriptTimeout();
		assertEventCounters("setScriptTimeout", 2, 0);
	}

	@Test(priority = 2)
	public void testImplicitlyWait() {
		setEventCounters();
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		assertEventCounters("implicitlyWait", 2, 0);
	}

	@Test(priority = 2)
	public void testPageLoadTimeout() {
		setEventCounters();
		wd.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
		assertEventCounters("pageLoadTimeout", 2, 0);
	}

	@Test(priority = 2)
	public void testTakeScreenshots() {
		setEventCounters();
		wd.getScreenshotAs(OutputType.FILE);
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.getScreenshotAs(OutputType.FILE);
		assertEventCounters("takeScreenshots", 6, 0);
	}

	@Test(priority = 2)
	public void testQuit() {
		setEventCounters();
		wd.quit();
		assertEventCounters("quit", 2, 0);
	}

	@Test(priority = 2)
	public void testWebDriverExceptionHandling() {
		setEventCounters();
		boolean wasExceptionThrown = false;
		MockCommandExecutor.setDoTriggerWebDriverException();
		// this command will not get executed due to a forced exception
		try {
			wd.get("https://www.salesforce.com");
		} catch (WebDriverException we) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertEventCounters("get", 2, 1);
	}

	/*
	 * RemoteAlert specific tests
	 */
	@Test(priority = 2)
	public void testGetAlertText() {
		setEventCounters();
		Alert alert = wd.switchTo().alert();
		assertNotNull(alert);
		assertEquals(alert.getText(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getTextByAlert", 2, 0);
	}

	@Test(priority = 2)
	public void testDismissAlert() {
		setEventCounters();
		Alert alert = wd.switchTo().alert();
		assertNotNull(alert);
		alert.dismiss();
		assertEventCounters("dismissByAlert", 2, 1);
	}

	@Test(priority = 2)
	public void testSecondWebDriver(){
		setEventCounters();
		wd.quit();
		MutableCapabilities mcap = new MutableCapabilities();
		mcap.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, "true");
		MockCommandExecutor mce = new MockCommandExecutor();
		wd = new MockRemoteWebDriver(mce, mcap);
		mce.setRemoteWebDriver(wd);

		setEventCounters();
		wd.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
		assertEventCounters("setScriptTimeout", 2, 0);
	}

	private void setEventCounters() {
		numOfEventsBefore = fullLogger.getListOfEventsRecorded().size();
		numOfScreenshotEventsBefore = screenshotLogger.getListOfEventsRecorded().size();
	}

	private void assertEventCounters(String method, int eventDelta, int screenshotDelta) {
		assertNumOfLogEntries(method, numOfEventsBefore, fullLogger.getListOfEventsRecorded().size(), eventDelta, "FullLogger");
		assertNumOfLogEntries(method, numOfScreenshotEventsBefore, screenshotLogger.getListOfEventsRecorded().size(), screenshotDelta, "ScreenshotLogger");
	}

	private void assertNumOfLogEntries(String command, int before, int after, int expectedDifference, String eventType) {
		System.out.println(String.format("Number of events logged by %s before %s: %d, and after: %d", eventType, command, before, after));
		Assert.assertTrue(after >= before);
		assertEquals(after - before , expectedDifference);
	}
}
