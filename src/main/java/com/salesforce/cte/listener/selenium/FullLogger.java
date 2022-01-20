/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.cte.listener.selenium;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v96.network.Network;
import org.openqa.selenium.devtools.v96.network.model.Headers;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.Augmenter;

import com.salesforce.cte.listener.selenium.WebDriverEvent.Cmd;

/**
 * Collects information on a given WebDriver command such as click() or getText() and saves this
 * collection to a JSON file.
 * 
 * The {@link org.openqa.selenium.remote.RemoteWebDriver}, {@link org.openqa.selenium.remote.RemoteWebElement},
 * {@link org.openqa.selenium.remote.RemoteKeyboard}, and {@link org.openqa.selenium.remote.RemoteMouse} have been patched
 * to meet Test Advisor's needs and call {@link com.salesforce.cte.listener.selenium.EventDispatcher} which creates {@link com.salesforce.cte.listener.selenium.WebDriverEvent}
 * objects before and after each WebDriver command. This class is a complete log all these Event objects.
 * 
 * @author gneumann
 * @since 1.0
 */
public class FullLogger extends AbstractEventListener {
	private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );
	
	private WebDriver driver;
	@Override
	public void setWebDriver(WebDriver driver){
		this.driver = driver;
	}
	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeClose(WebDriverEvent event) {
		logEntries.add(event);
		
	}

	@Override
	public void afterClose(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeFindElement(WebDriverEvent event, By by) {
		logEntries.add(event);
	}

	@Override
	public void afterFindElement(WebDriverEvent event, WebElement returnedElement, By by) {
		logEntries.add(event);
	}

	@Override
	public void beforeFindElements(WebDriverEvent event, By by) {
		logEntries.add(event);
	}

	@Override
	public void afterFindElements(WebDriverEvent event, List<WebElement> returnedElements, By by) {
		logEntries.add(event);
	}

	@Override
	public void beforeGet(WebDriverEvent event, String url) {
		logEntries.add(event);
		driver = new Augmenter().augment(driver);
		if (driver instanceof HasDevTools){
			DevTools devTools = ((HasDevTools) driver).getDevTools();
			devTools.createSession();
			devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
			HashMap<String, Object> headers = new HashMap<>();
			String traceID = administrator.getTestCaseExecution().getTraceId();
			LOGGER.log(Level.INFO, "Set trace id as {0}", traceID);
			headers.put("x-b3-traceid", traceID);
			headers.put("x-b3-spanid", traceID);
			headers.put("x-b3-sampled", "1");
			devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));
		}
	}

	@Override
	public void afterGet(WebDriverEvent event, String url) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetCurrentUrl(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetCurrentUrl(WebDriverEvent event, String url) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetTitle(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetTitle(WebDriverEvent event, String title) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetWindowHandle(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetWindowHandle(WebDriverEvent event, String handle) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetWindowHandles(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetWindowHandles(WebDriverEvent event, Set<String> handles) {
		logEntries.add(event);
	}

	@Override
	public void beforeQuit(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterQuit(WebDriverEvent event) {
		logEntries.add(event);
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called from WebDriver object
	 * after casting to RemoteWebDriver.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeActions(WebDriverEvent event, Collection<Sequence> actions) {
		logEntries.add(event);
	}

	@Override
	public void afterActions(WebDriverEvent event, Collection<Sequence> actions) {
		logEntries.add(event);
	}

	@Override
	public void beforePrint(WebDriverEvent event, PrintOptions printOptions) {
		logEntries.add(event);
	}

	@Override
	public void afterPrint(WebDriverEvent event, PrintOptions printOptions, Pdf printedPdfPage) {
		logEntries.add(event);
	}

	@Override
	public void beforeResetInputState(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterResetInputState(WebDriverEvent event) {
		logEntries.add(event);
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeExecuteAsyncScript(WebDriverEvent event, String script, List<Object> convertedArgs) {
		logEntries.add(event);
	}

	@Override
	public void afterExecuteAsyncScript(WebDriverEvent event, String script, List<Object> convertedArgs, Object result) {
		logEntries.add(event);
	}

	@Override
	public void beforeExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs) {
		logEntries.add(event);
	}

	@Override
	public void afterExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs, Object result) {
		logEntries.add(event);
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to TakesScreenshot.
	 *--------------------------------------------------------------------*/

	@Override
	public <X> void beforeGetScreenshotAs(WebDriverEvent event, OutputType<X> target) {
		logEntries.add(event);
	}

	@Override
	public <X> void afterGetScreenshotAs(WebDriverEvent event, OutputType<X> target, X screenshot) {
		logEntries.add(event);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeBack(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterBack(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeForward(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterForward(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeRefresh(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterRefresh(WebDriverEvent event) {
		logEntries.add(event);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeActiveElement(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterActiveElement(WebDriverEvent event, WebElement activeElement) {
		logEntries.add(event);
	}

	@Override
	public void beforeDefaultContent(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterDefaultContent(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeFrameByIndex(WebDriverEvent event, int frameIndex) {
		logEntries.add(event);
	}

	@Override
	public void afterFrameByIndex(WebDriverEvent event, int frameIndex) {
		logEntries.add(event);
	}

	@Override
	public void beforeFrameByElement(WebDriverEvent event, WebElement frameElement) {
		logEntries.add(event);
	}

	@Override
	public void afterFrameByElement(WebDriverEvent event, WebElement frameElement) {
		logEntries.add(event);
	}

	@Override
	public void beforeParentFrame(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterParentFrame(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeWindow(WebDriverEvent event, String windowName) {
		logEntries.add(event);
	}

	@Override
	public void afterWindow(WebDriverEvent event, String windowName) {
		logEntries.add(event);
	}

	@Override
	public void beforeNewWindow(WebDriverEvent event, WindowType typeHint) {
		logEntries.add(event);
	}

	@Override
	public void afterNewWindow(WebDriverEvent event, WindowType typeHint) {
		logEntries.add(event);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Timeouts object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeImplicitlyWait(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void afterImplicitlyWait(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetImplicitWaitTimeout(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetImplicitWaitTimeout(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void beforePageLoadTimeout(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void afterPageLoadTimeout(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetPageLoadTimeout(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetPageLoadTimeout(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void beforeSetScriptTimeout(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void afterSetScriptTimeout(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetScriptTimeout(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetScriptTimeout(WebDriverEvent event, Duration duration) {
		logEntries.add(event);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeFullscreen(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterFullscreen(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetPosition(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetPosition(WebDriverEvent event, Point targetPosition) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetSizeByWindow(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetSizeByWindow(WebDriverEvent event, Dimension targetSize) {
		logEntries.add(event);
	}

	@Override
	public void beforeMaximize(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterMaximize(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeMinimize(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterMinimize(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeSetPosition(WebDriverEvent event, Point targetPosition) {
		logEntries.add(event);
	}

	@Override
	public void afterSetPosition(WebDriverEvent event, Point targetPosition) {
		logEntries.add(event);
	}

	@Override
	public void beforeSetSizeByWindow(WebDriverEvent event, Dimension targetSize) {
		logEntries.add(event);
	}

	@Override
	public void afterSetSizeByWindow(WebDriverEvent event, Dimension targetSize) {
		logEntries.add(event);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeClick(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterClick(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeClear(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterClear(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetDomProperty(WebDriverEvent event, String name, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetDomProperty(WebDriverEvent event, String value, String name, WebElement element) { logEntries.add(event); }

	@Override
	public void beforeGetDomAttribute(WebDriverEvent event, String name, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetDomAttribute(WebDriverEvent event, String value, String name, WebElement element) { logEntries.add(event); }

	@Override
	public void beforeGetAttribute(WebDriverEvent event, String name, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetAttribute(WebDriverEvent event, String value, String name, WebElement element) { logEntries.add(event);	}

	@Override
	public void beforeGetAriaRole(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetAriaRole(WebDriverEvent event, String value, WebElement element) { logEntries.add(event);	}

	@Override
	public void beforeGetAccessibleName(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetAccessibleName(WebDriverEvent event, String value, WebElement element) { logEntries.add(event);	}

	@Override
	public void beforeGetCssValue(WebDriverEvent event, String propertyName, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetCssValue(WebDriverEvent event, String propertyName, String value, WebElement element) { logEntries.add(event); }

	@Override
	public void beforeGetTagName(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetTagName(WebDriverEvent event, String tagName, WebElement element) { logEntries.add(event); }

	@Override
	public void beforeGetText(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetText(WebDriverEvent event, String text, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeIsDisplayed(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterIsDisplayed(WebDriverEvent event, boolean isDisplayed, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeIsEnabled(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterIsEnabled(WebDriverEvent event, boolean isEnabled, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeIsSelected(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterIsSelected(WebDriverEvent event, boolean isSelected, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetLocation(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetLocation(WebDriverEvent event, Point point, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetSizeByElement(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetSizeByElement(WebDriverEvent event, Dimension dimension, WebElement element) { logEntries.add(event); }

	@Override
	public void beforeGetRect(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetRect(WebDriverEvent event, Rectangle rectangle, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeSendKeysByElement(WebDriverEvent event, WebElement element, CharSequence... keysToSend) {
		logEntries.add(event);
	}

	@Override
	public void afterSendKeysByElement(WebDriverEvent event, WebElement element, CharSequence... keysToSend) {
		logEntries.add(event);
	}

	@Override
	public void beforeSubmit(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterSubmit(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetShadowRoot(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetShadowRoot(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void beforeSendKeysByKeyboard(WebDriverEvent event, CharSequence... keysToSend) {
		logEntries.add(event);
	}

	@Override
	public void afterSendKeysByKeyboard(WebDriverEvent event, CharSequence... keysToSend) {
		logEntries.add(event);
	}

	@Override
	public void beforePressKey(WebDriverEvent event, CharSequence... keyToPress) {
		logEntries.add(event);
	}

	@Override
	public void afterPressKey(WebDriverEvent event, CharSequence... keyToPress) {
		logEntries.add(event);
	}

	@Override
	public void beforeReleaseKey(WebDriverEvent event, CharSequence... keyToPress) {
		logEntries.add(event);
	}

	@Override
	public void afterReleaseKey(WebDriverEvent event, CharSequence... keyToPress) {
		logEntries.add(event);
	}

	@Override
	public void beforeClickByMouse(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void afterClickByMouse(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void beforeDoubleClick(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void afterDoubleClick(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void beforeMouseDown(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void afterMouseDown(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void beforeMouseUp(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void afterMouseUp(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void beforeMouseMove(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void afterMouseMove(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void beforeMouseMove(WebDriverEvent event, Coordinates where, long xOffset, long yOffset) {
		logEntries.add(event);
	}

	@Override
	public void afterMouseMove(WebDriverEvent event, Coordinates where, long xOffset, long yOffset) {
		logEntries.add(event);
	}

	@Override
	public void beforeContextClick(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void afterContextClick(WebDriverEvent event, Coordinates where) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetPageSource(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetPageSource(WebDriverEvent event, String source) {
		logEntries.add(event);
	}

	@Override
	public void beforeAddCookie(WebDriverEvent event, Cookie cookie) {
		logEntries.add(event);
	}

	@Override
	public void afterAddCookie(WebDriverEvent event, Cookie cookie) {
		logEntries.add(event);
	}

	@Override
	public void beforeDeleteCookieNamed(WebDriverEvent event, String name) {
		logEntries.add(event);
	}

	@Override
	public void afterDeleteCookieNamed(WebDriverEvent event, String name) {
		logEntries.add(event);
	}

	@Override
	public void beforeDeleteCookie(WebDriverEvent event, Cookie cookie) {
		logEntries.add(event);
	}

	@Override
	public void afterDeleteCookie(WebDriverEvent event, Cookie cookie) {
		logEntries.add(event);
	}

	@Override
	public void beforeDeleteAllCookies(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterDeleteAllCookies(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetCookies(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetCookies(WebDriverEvent event, Set<Cookie> cookies) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetCookieNamed(WebDriverEvent event, String name) {
		logEntries.add(event);
	}

	@Override
	public void afterGetCookieNamed(WebDriverEvent event, String name, Cookie cookie) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetAvailableEngines(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetAvailableEngines(WebDriverEvent event, List<String> engines) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetActiveEngine(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetActiveEngine(WebDriverEvent event, String engine) {
		logEntries.add(event);
	}

	@Override
	public void beforeIsActivated(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterIsActivated(WebDriverEvent event, boolean isActive) {
		logEntries.add(event);
	}

	@Override
	public void beforeDeactivate(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterDeactivate(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeActivateEngine(WebDriverEvent event, String engine) {
		logEntries.add(event);
	}

	@Override
	public void afterActivateEngine(WebDriverEvent event, String engine) {
		logEntries.add(event);
	}

	@Override
	public void beforeDismiss(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterDismiss(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeAccept(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterAccept(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetTextByAlert(WebDriverEvent event) {
		logEntries.add(event);
	}

	@Override
	public void afterGetTextByAlert(WebDriverEvent event, String text) {
		logEntries.add(event);
	}

	@Override
	public void beforeSendKeysByAlert(WebDriverEvent event, String keysToSend) {
		logEntries.add(event);
	}

	@Override
	public void afterSendKeysByAlert(WebDriverEvent event, String keysToSend) {
		logEntries.add(event);
	}

	@Override
	public void beforeGetCoordinates(WebDriverEvent event, WebElement element) {
		logEntries.add(event);
	}

	@Override
	public void afterGetCoordinates(WebDriverEvent event, Coordinates coordinates, WebElement element) { logEntries.add(event); }

	@Override
	public <X> void beforeGetScreenshotAsByElement(WebDriverEvent event, OutputType<X> target, WebElement element) { logEntries.add(event); }

	@Override
	public <X> void afterGetScreenshotAsByElement(WebDriverEvent event, OutputType<X> target, X screenshot, WebElement element) { logEntries.add(event); }

	@Override
	public void beforeUploadFile(WebDriverEvent event, WebElement element, File localFile) { logEntries.add(event); }

	@Override
	public void afterUploadFile(WebDriverEvent event, WebElement element, File localFile, String response) { logEntries.add(event); }

	@Override
	public void onException(WebDriverEvent event, Cmd cmd, Throwable issue) {
		logEntries.add(event);
		administrator.getTestCaseExecution().appendEvent(createTestEvent(event, Level.WARNING));
	}
}
