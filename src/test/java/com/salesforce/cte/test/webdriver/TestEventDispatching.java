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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.salesforce.cte.admin.TestAdvisorAdministrator;
import com.salesforce.cte.common.TestEventType;
import com.salesforce.cte.listener.selenium.EventDispatcher;
import com.salesforce.cte.listener.selenium.FullListener;
import com.salesforce.cte.listener.selenium.IEventListener;
import com.salesforce.cte.listener.selenium.ScreenshotListener;
import com.salesforce.cte.listener.selenium.TestStepListener;
import com.salesforce.cte.listener.testng.TestListener;

/**
 * @author gneumann
 *
 */
@Listeners(TestListener.class)
public class TestEventDispatching {
    private TestAdvisorAdministrator administrator = TestAdvisorAdministrator.getInstance();

	private static MockRemoteWebDriver wd;
	private static FullListener fullListener;
	private static ScreenshotListener screenshotListener;
    private static TestStepListener testStepListener;

	private static int numOfEventsBefore;
	private static int numOfScreenshotEventsBefore;
    private static int numOfTestStepEventsBefore;

	@BeforeClass
	public static void setUpBeforeClass() {
		MutableCapabilities mcap = new MutableCapabilities();
		mcap.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, "true");
		MockCommandExecutor mce = new MockCommandExecutor();
		wd = new MockRemoteWebDriver(mce, mcap);
		mce.setRemoteWebDriver(wd);
		List<IEventListener> eventListeners = EventDispatcher.getInstance().getImmutableListOfEventListeners();
		for (IEventListener listener : eventListeners) {
			if (listener instanceof FullListener) {
				fullListener = (FullListener) listener;
			}
			if (listener instanceof ScreenshotListener){
				screenshotListener = (ScreenshotListener) listener;
			}
            if (listener instanceof TestStepListener){
                testStepListener = (TestStepListener) listener;
            }
		}
		System.setProperty("testadvisor.capturescreenshot", "true");
	}

    @BeforeMethod
    public void setup(){
        administrator.getTestResult().getTestCaseExecutionList().stream()
            .forEach(testcase -> testcase.getEventList().clear());
    }

	//@Test(priority = 1)
	public void testOnExceptionWithNoCurrentEventSet() {
		setEventCounters();
		EventDispatcher.getInstance().onException(null, null);
		assertEventCounters("click", 0, 0, 0);
	}

	// Test ScreenshotLogger
	@Test(priority = 2)
	public void testClick() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		we.click();
		assertEventCounters("click", 4, 1, 1);

		assertEquals(administrator.getTestCaseExecution().getEventList().get(0).getEventLevel(), Level.INFO.toString());
		assertEquals(administrator.getTestCaseExecution().getEventList().get(0).getSeleniumCmd(), "webElement.click");
		assertEquals(administrator.getTestCaseExecution().getEventList().get(0).getSeleniumLocator(), "By.id(\"someId\")");
		assertEquals(administrator.getTestCaseExecution().getEventList().get(0).getEventType(), TestEventType.SCREEN_SHOT);
		Assert.assertTrue(administrator.getTestCaseExecution().getEventList().get(0).getScreenshotPath().contains("screenshot"));
		Assert.assertTrue(administrator.getTestCaseExecution().getEventList().get(0).getScreenshotRecordNumber()>=0);

        assertEquals(administrator.getTestCaseExecution().getEventList().get(1).getEventType(), TestEventType.URL);
        assertEquals(administrator.getTestCaseExecution().getEventList().get(1).getEventLevel(), Level.INFO.toString());
	}

	@Test(priority = 2)
	public void testClickByChildElement() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		WebElement childWe = we.findElement(By.id("someOtherId"));
		assertNotNull(childWe);
		setEventCounters();
		childWe.click();
		assertEventCounters("clickByChildElement", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testGet() {
		setEventCounters();
		wd.get("https://www.salesforce.com");
		assertEventCounters("get", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testExecuteScriptWithScreenshot() {
		setEventCounters();
		wd.executeScript("click");
		assertEventCounters("executeScript", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testExecuteScriptWithoutScreenshot() {
		setEventCounters();
		String response = (String) wd.executeScript("style.border='3px solid'");
		assertEquals(response, "highlighted web element");
		assertEventCounters("executeScript", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testSubmit() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		we.submit();
		assertEventCounters("submit", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testSendKeysByElement() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		we.sendKeys("abc");
		assertEventCounters("sendKeys", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testClear() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		we.clear();
		assertEventCounters("clear", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testGetTagName() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getTagName(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getTagName", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetTextByElement() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getText(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getTextByElement", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetAttribute() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getAttribute("someAttribute"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getAttribute", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetDomProperty() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getDomProperty("someDomProperty"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getDomProperty", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetDomAttribute() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getDomAttribute("someDomAttribute"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getDomAttribute", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetAriaRole() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getAriaRole(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getAriaRole", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetAccessibleName() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getAccessibleName(), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getAccessibleName", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testIsSelected() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertTrue(we.isSelected());
		assertEventCounters("isSelected", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testIsSelectedWithIncorrectReturnType() {
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		boolean wasExceptionThrown = false;
		try {
			we.isSelected();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertEventCounters("isSelected", 1, 0, 0);
	}

	@Test(priority = 2)
	public void testIsEnabled() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertTrue(we.isEnabled());
		assertEventCounters("isEnabled", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testIsEnabledWithIncorrectReturnType() {
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		boolean wasExceptionThrown = false;
		try {
			we.isEnabled();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertEventCounters("isEnabled", 1, 0, 0);
	}

	@Test(priority = 2)
	public void testIsDisplayed() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertTrue(we.isDisplayed());
		assertEventCounters("isDisplayed", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testIsDisplayedWithIncorrectReturnType() {
		MockCommandExecutor.setReturnValue("This is not the right return type");
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		boolean wasExceptionThrown = false;
		try {
			we.isDisplayed();
		} catch (WebDriverException wde) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "WebDriverException not thrown as expected");
		assertEventCounters("isDisplayed", 1, 0, 0);
	}

	@Test(priority = 2)
	public void testGetCssValue() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Assert.assertEquals(we.getCssValue("someCssValue"), MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getCssValue", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetLocation() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Point point = we.getLocation();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertEventCounters("getLocation", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetSizeByElement() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Dimension size = we.getSize();
		Assert.assertEquals(size.getWidth(), 0);
		Assert.assertEquals(size.getHeight(), 0);
		assertEventCounters("getSizeByElement", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetRect() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		Rectangle rect = we.getRect();
		Assert.assertEquals(rect.getX(), 0);
		Assert.assertEquals(rect.getY(), 0);
		Assert.assertEquals(rect.getWidth(), 0);
		Assert.assertEquals(rect.getHeight(), 0);
		assertEventCounters("getRect", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesInViewPort() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		setEventCounters();
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		Point point = coordinates.inViewPort();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertEventCounters("getCoordinatesInViewPort", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesOnPage() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		setEventCounters();
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		Point point = coordinates.onPage();
		Assert.assertEquals(point.getX(), 0);
		Assert.assertEquals(point.getY(), 0);
		assertEventCounters("getCoordinatesOnPage", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesOnScreen() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		setEventCounters();
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		boolean wasExceptionThrown = false;
		try {
			coordinates.onScreen();
		} catch (UnsupportedOperationException uoe) {
			wasExceptionThrown = true;
		}
		Assert.assertTrue(wasExceptionThrown, "UnsupportedOperationException not thrown as expected");
		assertEventCounters("getCoordinatesOnScreen", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetCoordinatesGetAuxiliary() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		Assert.assertTrue(we instanceof RemoteWebElement);
		setEventCounters();
		Coordinates coordinates = ((RemoteWebElement) we).getCoordinates();
		assertNotNull(coordinates);
		String id = (String) coordinates.getAuxiliary();
		Assert.assertTrue(id.startsWith(MockCommandExecutor.ELEMENT_ID_PREFIX));
		assertEventCounters("getCoordinatesGetAuxiliary", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetShadowRoot() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		SearchContext sc = we.getShadowRoot();
		Assert.assertNotNull(sc);
		Assert.assertSame(sc, wd);
		assertEventCounters("getShadowRoot", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testSendKeysByElementSendingCharsOneByOne() {
		WebElement we = wd.findElement(By.id("someOtherId"));
		assertNotNull(we);
		setEventCounters();
		we.sendKeys("a");
		we.sendKeys("b");
		we.sendKeys("c");
		assertEventCounters("sendKeys", 8, 1, 1);
	}

	@Test(priority = 2)
	public void testClose() {
		setEventCounters();
		wd.close();
		assertEventCounters("close", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testTo() {
		setEventCounters();
		wd.navigate().to("http://somewhere");
		assertEventCounters("to", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testToURL() throws MalformedURLException {
		setEventCounters();
		wd.navigate().to(new URL("http://somewhere"));
		assertEventCounters("toURL", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testBack() {
		setEventCounters();
		wd.navigate().back();
		assertEventCounters("back", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testForward() {
		setEventCounters();
		wd.navigate().forward();
		assertEventCounters("forward", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testDismiss() {
		setEventCounters();
		wd.switchTo().alert().dismiss();
		assertEventCounters("dismiss", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testAccept() {
		setEventCounters();
		wd.switchTo().alert().accept();
		assertEventCounters("accept", 4, 1, 1);
	}

	@Test(priority = 2)
	public void testSendKeysToAlert() {
		setEventCounters();
		wd.switchTo().alert().sendKeys("some");
		assertEventCounters("sendKeysToAlert", 4, 1, 1);
	}

	// Test FullLogger
	@Test(priority = 2)
	public void testFindElementByWebDriver() {
		setEventCounters();
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		assertEventCounters("findElementByWebDriver", 2, 0, 0);
	}
	
	@Test(priority = 2)
	public void testFindElementByElement() {
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		setEventCounters();
		WebElement childWe = we.findElement(By.id("someOtherId"));
		assertNotNull(childWe);
		assertEventCounters("findElementByElement", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetTitle() {
		setEventCounters();
		assertEquals(MockCommandExecutor.STATE_OK, wd.getTitle());
		assertEventCounters("getTitle", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testExecuteAsyncScript() {
		setEventCounters();
		wd.executeAsyncScript("some script");
		assertEventCounters("executeAsyncScript", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testRefresh() {
		setEventCounters();
		wd.navigate().refresh();
		assertEventCounters("refresh", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testActiveElement() {
		setEventCounters();
		WebElement elem = wd.switchTo().activeElement();
		assertNotNull(elem);
		assertEventCounters("activeElement", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testDefaultContent() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().defaultContent();
		assertNotNull(returnedWd);
		assertEventCounters("defaultContent", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testFrameByIndex() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().frame(0);
		assertNotNull(returnedWd);
		assertEventCounters("frameByIndex", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testFrameByName() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().frame(MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNotNull(returnedWd);
		assertEventCounters("frameByName", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testFrameByElement() {
		WebElement elem = wd.findElement(By.id("someId"));
		assertNotNull(elem);
		setEventCounters();
		assertNotNull(wd.switchTo().frame(elem));
		assertEventCounters("frameByElement", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testParentFrame() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().parentFrame();
		assertNotNull(returnedWd);
		assertEventCounters("parentFrame", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testWindow() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().window(MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertNotNull(returnedWd);
		assertEventCounters("window", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testNewWindow() {
		setEventCounters();
		WebDriver returnedWd = wd.switchTo().newWindow(WindowType.WINDOW);
		assertNotNull(returnedWd);
		assertEventCounters("newWindow", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetURL() {
		setEventCounters();
		wd.getCurrentUrl();
		assertEventCounters("getURL", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testScreenshot() {
		setEventCounters();
		wd.getScreenshotAs(OutputType.FILE);
		assertEventCounters("screenshot", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetPageSource() {
		setEventCounters();
		String source = wd.getPageSource();
		assertNotNull(source);
		Assert.assertEquals(source, MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getPageSource", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testWindowHandle() {
		setEventCounters();
		String handle = wd.getWindowHandle();
		assertNotNull(handle);
		Assert.assertEquals(handle, MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("getWindowHandle", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testDeleteAllCookies() {
		setEventCounters();
		wd.manage().deleteAllCookies();
		assertEventCounters("deleteAllCookies", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testDeleteCookieNamed() {
		setEventCounters();
		wd.manage().deleteCookieNamed("name");
		assertEventCounters("deleteCookieNamed", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testAddCookie() {
		setEventCounters();
		wd.manage().addCookie(new Cookie("name", "value"));
		assertEventCounters("addCookie", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetCookies() {
		setEventCounters();
		Set<Cookie> cookies = wd.manage().getCookies();
		assertNotNull(cookies);
		assertEventCounters("getCookies", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetCookie() {
		setEventCounters();
		wd.manage().getCookieNamed("name");
		assertEventCounters("getCookie", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testDeleteCookies() {
		setEventCounters();
		wd.manage().deleteCookieNamed("name");
		assertEventCounters("deleteCookie", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testSetTimeout() {
		setEventCounters();
		wd.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
		assertEventCounters("setScriptTimeout", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetScriptTimeout() {
		setEventCounters();
		Duration timeout = wd.manage().timeouts().getScriptTimeout();
		assertEquals(timeout.getSeconds(), 3L);
		assertEventCounters("getScriptTimeout", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testImplicitlyWait() {
		setEventCounters();
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		assertEventCounters("implicitlyWait", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetImplicitWaitTimeout() {
		setEventCounters();
		Duration timeout = wd.manage().timeouts().getImplicitWaitTimeout();
		assertEquals(timeout.getSeconds(), 1L);
		assertEventCounters("getImplicitWaitTimeout", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testPageLoadTimeout() {
		setEventCounters();
		wd.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
		assertEventCounters("pageLoadTimeout", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetPageLoadTimeout() {
		setEventCounters();
		Duration timeout = wd.manage().timeouts().getPageLoadTimeout();
		assertEquals(timeout.getSeconds(), 2L);
		assertEventCounters("getPageLoadTimeout", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testDoNotTakeScreenshotsOnExplicitTakeScreenshots() {
		setEventCounters();
		wd.getScreenshotAs(OutputType.FILE);
		WebElement we = wd.findElement(By.id("someId"));
		assertNotNull(we);
		we.getScreenshotAs(OutputType.FILE);
		assertEventCounters("takeScreenshots", 6, 0, 0);
	}

	@Test(priority = 2)
	public void testQuit() {
		setEventCounters();
		wd.quit();
		assertEventCounters("quit", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testMaximize() {
		setEventCounters();
		wd.manage().window().maximize();
		assertEventCounters("manage.window.maximize", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testMinimize() {
		setEventCounters();
		wd.manage().window().minimize();
		assertEventCounters("manage.window.minimize", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testFullscreen() {
		setEventCounters();
		wd.manage().window().fullscreen();
		assertEventCounters("manage.window.fullscreen", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetPosition() {
		setEventCounters();
		Point position = wd.manage().window().getPosition();
		assertNotNull(position);
		assertEquals(position.getX(), 0);
		assertEquals(position.getY(), 0);
		assertEventCounters("manage.window.getPosition", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testSetSize() {
		setEventCounters();
		wd.manage().window().setSize(new Dimension(1024, 768));
		assertEventCounters("manage.window.setSize", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testSetPosition() {
		setEventCounters();
		wd.manage().window().setPosition(new Point(0, 0));
		assertEventCounters("manage.window.setPosition", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testGetSizeByWindow() {
		setEventCounters();
		Dimension size = wd.manage().window().getSize();
		assertNotNull(size);
		assertEquals(size.getHeight(), 0);
		assertEquals(size.getWidth(), 0);
		assertEventCounters("manage.window.getSize", 2, 0, 0);
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
		assertEventCounters("get", 2, 1, 0);
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
		assertEventCounters("getTextByAlert", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testDismissAlert() {
		setEventCounters();
		Alert alert = wd.switchTo().alert();
		assertNotNull(alert);
		alert.dismiss();
		assertEventCounters("dismissByAlert", 4, 1, 1);
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
		assertEventCounters("setScriptTimeout", 2, 0, 0);
	}

	/*
	 * Tests for RemoteKeyboard
	 */
	@Test(priority = 2)
	public void testSendKeysByKeyboard() {
		setEventCounters();
		wd.getKeyboard().sendKeys(MockCommandExecutor.STRING_ALLISWELL_VALUE);
		assertEventCounters("keyboard.sendKeys", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testPressKey() {
		setEventCounters();
		wd.getKeyboard().pressKey("a");
		assertEventCounters("keyboard.pressKey", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testReleaseKey() {
		setEventCounters();
		wd.getKeyboard().releaseKey("a");
		assertEventCounters("keyboard.releaseKey", 2, 0, 0);
	}

	/*
	 * Tests for RemoteMouse
	 */
	@Test(priority = 2)
	public void testClickByMouse() {
		setEventCounters();
		wd.getMouse().click(COORDINATE_0_0);
		assertEventCounters("mouse.click", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testContextClick() {
		setEventCounters();
		wd.getMouse().contextClick(COORDINATE_0_0);
		assertEventCounters("mouse.contextClick", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testDoubleClick() {
		setEventCounters();
		wd.getMouse().doubleClick(COORDINATE_0_0);
		assertEventCounters("mouse.doubleClick", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testMouseDown() {
		setEventCounters();
		wd.getMouse().mouseDown(COORDINATE_0_0);
		assertEventCounters("mouse.mouseDown", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testMouseUp() {
		setEventCounters();
		wd.getMouse().mouseUp(COORDINATE_0_0);
		assertEventCounters("mouse.mouseUp", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testMouseMove() {
		setEventCounters();
		wd.getMouse().mouseMove(COORDINATE_0_0);
		assertEventCounters("mouse.mouseMove", 2, 0, 0);
	}

	@Test(priority = 2)
	public void testMouseMoveWithOffset() {
		setEventCounters();
		wd.getMouse().mouseMove(COORDINATE_0_0, 0, 0);
		assertEventCounters("mouse.mouseMoveWithOffset", 2, 0, 0);
	}

	private void setEventCounters() {
		numOfEventsBefore = fullListener.getListOfEventsRecorded().size();
		numOfScreenshotEventsBefore = screenshotListener.getListOfEventsRecorded().size();
        numOfTestStepEventsBefore = testStepListener.getListOfEventsRecorded().size();
	}

	private void assertEventCounters(String method, int eventDelta, int screenshotDelta, int testStepDelta) {
		assertNumOfLogEntries(method, numOfEventsBefore, fullListener.getListOfEventsRecorded().size(), eventDelta, "FullListener");
		assertNumOfLogEntries(method, numOfScreenshotEventsBefore, screenshotListener.getListOfEventsRecorded().size(), screenshotDelta, "ScreenshotListener");
        assertNumOfLogEntries(method, numOfTestStepEventsBefore, testStepListener.getListOfEventsRecorded().size(), testStepDelta, "TestStepListener");
	}

	private void assertNumOfLogEntries(String command, int before, int after, int expectedDifference, String eventType) {
		String msg = String.format("Number of events logged by %s before %s: %d, and after: %d did not match expected difference %d", eventType, command, before, after, expectedDifference);
		Assert.assertTrue(after >= before, msg);
		assertEquals(after - before , expectedDifference, msg);
	}

	private static final Coordinates COORDINATE_0_0 = new Coordinates() {
		@Override
		public Point onScreen() { return new Point(0, 0); }
		@Override
		public Point inViewPort() { return new Point(0, 0); }
		@Override
		public Point onPage() { return new Point(0, 0); }
		@Override
		public Object getAuxiliary() { return "zero"; }
	};
}
