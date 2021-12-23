//Licensed to the Software Freedom Conservancy (SFC) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The SFC licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.
package com.salesforce.cte.listener.selenium;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.print.PrintOptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salesforce.cte.admin.TestAdvisorAdministrator;
import com.salesforce.cte.common.TestEvent;
import com.salesforce.cte.listener.selenium.WebDriverEvent.Cmd;

/**
 * Use this class as base class, if you want to implement a
 * {@link IEventListener} and are only interested in some events. All
 * methods provided by this class have an empty method body.
 * 
 * This is an extended version of org.openqa.selenium.support.events.AbstractWebDriverEventListener. See
 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/events/AbstractWebDriverEventListener.html
 * for more information.
 * 
 * @since 1.0
 */
public abstract class AbstractEventListener implements IEventListener {
	@JsonProperty("logEntries")
	protected List<WebDriverEvent> logEntries = new ArrayList<>();
	protected TestAdvisorAdministrator administrator = TestAdvisorAdministrator.getInstance();

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeClose(WebDriverEvent event) {
	}

	@Override
	public void afterClose(WebDriverEvent event) {
	}

	@Override
	public void beforeFindElement(WebDriverEvent event, By by) {
	}

	@Override
	public <T extends WebElement> void afterFindElement(WebDriverEvent event, T returnedElement, By by) {
	}

	@Override
	public void beforeFindElements(WebDriverEvent event, By by) {
	}

	@Override
	public <T extends WebElement> void afterFindElements(WebDriverEvent event, List<T> returnedElements, By by) {
	}

	@Override
	public void beforeGet(WebDriverEvent event, String url) {
	}

	@Override
	public void afterGet(WebDriverEvent event, String url) {
	}

	@Override
	public void beforeGetCurrentUrl(WebDriverEvent event) {
	}

	@Override
	public void afterGetCurrentUrl(WebDriverEvent event, String url) {
	}

	@Override
	public void beforeGetPageSource(WebDriverEvent event) {
	}

	@Override
	public void afterGetPageSource(WebDriverEvent event, String source) {
	}

	@Override
	public void beforeGetTitle(WebDriverEvent event) {
	}

	@Override
	public void afterGetTitle(WebDriverEvent event, String title) {
	}

	@Override
	public void beforeGetWindowHandle(WebDriverEvent event) {
	}

	@Override
	public void afterGetWindowHandle(WebDriverEvent event, String handle) {
	}

	@Override
	public void beforeGetWindowHandles(WebDriverEvent event) {
	}

	@Override
	public void afterGetWindowHandles(WebDriverEvent event, Set<String> handles) {
	}

	@Override
	public void beforeQuit(WebDriverEvent event) {
	}

	@Override
	public void afterQuit(WebDriverEvent event) {
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called from WebDriver object
	 * after casting to RemoteWebDriver.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeActions(WebDriverEvent event, Collection<Sequence> actions) {
	}

	@Override
	public void afterActions(WebDriverEvent event, Collection<Sequence> actions) {
	}

	@Override
	public void beforePrint(WebDriverEvent event, PrintOptions printOptions) {
	}

	@Override
	public void afterPrint(WebDriverEvent event, PrintOptions printOptions, Pdf printedPdfPage) {
	}

	@Override
	public void beforeResetInputState(WebDriverEvent event) {
	}

	@Override
	public void afterResetInputState(WebDriverEvent event) {
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeExecuteAsyncScript(WebDriverEvent event, String script, List<Object> convertedArgs) {
	}

	@Override
	public void afterExecuteAsyncScript(WebDriverEvent event, String script, List<Object> convertedArgs, Object result) {
	}

	@Override
	public void beforeExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs) {
	}

	@Override
	public void afterExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs, Object result) {
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to TakesScreenshot.
	 *--------------------------------------------------------------------*/

	@Override
	public <X> void beforeGetScreenshotAs(WebDriverEvent event, OutputType<X> target) {
	}

	@Override
	public <X> void afterGetScreenshotAs(WebDriverEvent event, OutputType<X> target, X screenshot) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeBack(WebDriverEvent event) {
	}

	@Override
	public void afterBack(WebDriverEvent event) {
	}

	@Override
	public void beforeForward(WebDriverEvent event) {
	}

	@Override
	public void afterForward(WebDriverEvent event) {
	}

	@Override
	public void beforeRefresh(WebDriverEvent event) {
	}

	@Override
	public void afterRefresh(WebDriverEvent event) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeActiveElement(WebDriverEvent event) {
	}

	@Override
	public <T extends WebElement> void afterActiveElement(WebDriverEvent event, T activeElement) {
	}

	@Override
	public void beforeDefaultContent(WebDriverEvent event) {
	}

	@Override
	public void afterDefaultContent(WebDriverEvent event) {
	}

	@Override
	public void beforeFrameByIndex(WebDriverEvent event, int frameIndex) {
	}

	@Override
	public void afterFrameByIndex(WebDriverEvent event, int frameIndex) {
	}

	@Override
	public <T extends WebElement> void beforeFrameByElement(WebDriverEvent event, T frameElement) {
	}

	@Override
	public <T extends WebElement> void afterFrameByElement(WebDriverEvent event, T frameElement) {
	}

	@Override
	public void beforeParentFrame(WebDriverEvent event) {
	}

	@Override
	public void afterParentFrame(WebDriverEvent event) {
	}

	@Override
	public void beforeWindow(WebDriverEvent event, String windowName) {
	}

	@Override
	public void afterWindow(WebDriverEvent event, String windowName) {
	}

	@Override
	public void beforeNewWindow(WebDriverEvent event, WindowType typeHint) {
	}

	@Override
	public void afterNewWindow(WebDriverEvent event, WindowType typeHint) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Timeouts object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeImplicitlyWait(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void afterImplicitlyWait(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void beforeGetImplicitWaitTimeout(WebDriverEvent event) {
	}

	@Override
	public void afterGetImplicitWaitTimeout(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void beforePageLoadTimeout(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void afterPageLoadTimeout(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void beforeGetPageLoadTimeout(WebDriverEvent event) {
	}

	@Override
	public void afterGetPageLoadTimeout(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void beforeSetScriptTimeout(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void afterSetScriptTimeout(WebDriverEvent event, Duration duration) {
	}

	@Override
	public void beforeGetScriptTimeout(WebDriverEvent event) {
	}

	@Override
	public void afterGetScriptTimeout(WebDriverEvent event, Duration duration) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeFullscreen(WebDriverEvent event) {
	}

	@Override
	public void afterFullscreen(WebDriverEvent event) {
	}

	@Override
	public void beforeGetPosition(WebDriverEvent event) {
	}

	@Override
	public void afterGetPosition(WebDriverEvent event, Point targetPosition) {
	}

	@Override
	public void beforeGetSizeByWindow(WebDriverEvent event) {
	}

	@Override
	public void afterGetSizeByWindow(WebDriverEvent event, Dimension targetSize) {
	}

	@Override
	public void beforeMaximize(WebDriverEvent event) {
	}

	@Override
	public void afterMaximize(WebDriverEvent event) {
	}

	@Override
	public void beforeMinimize(WebDriverEvent event) {
	}

	@Override
	public void afterMinimize(WebDriverEvent event) {
	}

	@Override
	public void beforeSetPosition(WebDriverEvent event, Point targetPosition) {
	}

	@Override
	public void afterSetPosition(WebDriverEvent event, Point targetPosition) {
	}

	@Override
	public void beforeSetSizeByWindow(WebDriverEvent event, Dimension targetSize) {
	}

	@Override
	public void afterSetSizeByWindow(WebDriverEvent event, Dimension targetSize) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	@Override
	public <T extends WebElement> void beforeClick(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterClick(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void beforeClear(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterClear(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetAttribute(WebDriverEvent event, String name, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetAttribute(WebDriverEvent event, String value, String name, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetDomProperty(WebDriverEvent event, String name, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetDomProperty(WebDriverEvent event, String value, String name, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetDomAttribute(WebDriverEvent event, String name, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetDomAttribute(WebDriverEvent event, String value, String name, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetCssValue(WebDriverEvent event, String propertyName, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetAriaRole(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetAriaRole(WebDriverEvent event, String role, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetAccessibleName(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetAccessibleName(WebDriverEvent event, String name, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetCssValue(WebDriverEvent event, String propertyName, String value, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetTagName(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetTagName(WebDriverEvent event, String tagName, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetText(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetText(WebDriverEvent event, String text, T element) {
	}

	@Override
	public <T extends WebElement> void beforeIsDisplayed(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterIsDisplayed(WebDriverEvent event, boolean isDisplayed, T element) {
	}

	@Override
	public <T extends WebElement> void beforeIsEnabled(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterIsEnabled(WebDriverEvent event, boolean isEnabled, T element) {
	}

	@Override
	public <T extends WebElement> void beforeIsSelected(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterIsSelected(WebDriverEvent event, boolean isSelected, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetLocation(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetLocation(WebDriverEvent event, Point point, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetSizeByElement(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetSizeByElement(WebDriverEvent event, Dimension dimension, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetRect(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetRect(WebDriverEvent event, Rectangle rectangle, T element) {
	}

	@Override
	public <T extends WebElement> void beforeSendKeysByElement(WebDriverEvent event, T element, CharSequence... keysToSend) {
	}

	@Override
	public <T extends WebElement> void afterSendKeysByElement(WebDriverEvent event, T element, CharSequence... keysToSend) {
	}

	@Override
	public <T extends WebElement> void beforeSubmit(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterSubmit(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void beforeGetShadowRoot(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetShadowRoot(WebDriverEvent event, T element) {
	}

	@Override
	public void beforeSendKeysByKeyboard(WebDriverEvent event, CharSequence... keysToSend) {
	}

	@Override
	public void afterSendKeysByKeyboard(WebDriverEvent event, CharSequence... keysToSend) {
	}

	@Override
	public void beforePressKey(WebDriverEvent event, CharSequence... keyToPress) {
	}

	@Override
	public void afterPressKey(WebDriverEvent event, CharSequence... keyToPress) {
	}

	@Override
	public void beforeReleaseKey(WebDriverEvent event, CharSequence... keyToPress) {
	}

	@Override
	public void afterReleaseKey(WebDriverEvent event, CharSequence... keyToPress) {
	}

	@Override
	public void beforeClickByMouse(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void afterClickByMouse(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void beforeDoubleClick(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void afterDoubleClick(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void beforeMouseDown(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void afterMouseDown(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void beforeMouseUp(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void afterMouseUp(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void beforeMouseMove(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void afterMouseMove(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void beforeMouseMove(WebDriverEvent event, Coordinates where, long xOffset, long yOffset) {
	}

	@Override
	public void afterMouseMove(WebDriverEvent event, Coordinates where, long xOffset, long yOffset) {
	}

	@Override
	public void beforeContextClick(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void afterContextClick(WebDriverEvent event, Coordinates where) {
	}

	@Override
	public void onException(WebDriverEvent event, Cmd cmd, Throwable issue) {
	}

	/**
	 * The test name can contain characters which are not supported in a file name. This
	 * convenience method replaces such characters with underscores.
	 * @param testName test name
	 * @return file name using only characters supported by the OS
	 */
	public static String convertTestname2FileName(final String testName) {
		return testName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
	}

	@Override
	public void beforeAddCookie(WebDriverEvent event, Cookie cookie) {
	}

	@Override
	public void afterAddCookie(WebDriverEvent event, Cookie cookie) {
	}

	@Override
	public void beforeDeleteCookieNamed(WebDriverEvent event, String name) {
	}

	@Override
	public void afterDeleteCookieNamed(WebDriverEvent event, String name) {
	}

	@Override
	public void beforeDeleteCookie(WebDriverEvent event, Cookie cookie) {
	}

	@Override
	public void afterDeleteCookie(WebDriverEvent event, Cookie cookie) {
	}

	@Override
	public void beforeDeleteAllCookies(WebDriverEvent event) {
	}

	@Override
	public void afterDeleteAllCookies(WebDriverEvent event) {
	}

	@Override
	public void beforeGetCookies(WebDriverEvent event) {
	}

	@Override
	public void afterGetCookies(WebDriverEvent event, Set<Cookie> cookies) {
	}

	@Override
	public void beforeGetCookieNamed(WebDriverEvent event, String name) {
	}

	@Override
	public void afterGetCookieNamed(WebDriverEvent event, String name, Cookie cookie) {
	}

	@Override
	public void beforeGetAvailableEngines(WebDriverEvent event) {
	}

	@Override
	public void afterGetAvailableEngines(WebDriverEvent event, List<String> engines) {
	}

	@Override
	public void beforeGetActiveEngine(WebDriverEvent event) {
	}

	@Override
	public void afterGetActiveEngine(WebDriverEvent event, String engine) {
	}

	@Override
	public void beforeIsActivated(WebDriverEvent event) {
	}

	@Override
	public void afterIsActivated(WebDriverEvent event, boolean isActive) {
	}

	@Override
	public void beforeDeactivate(WebDriverEvent event) {
	}

	@Override
	public void afterDeactivate(WebDriverEvent event) {
	}

	@Override
	public void beforeActivateEngine(WebDriverEvent event, String engine) {
	}

	@Override
	public void afterActivateEngine(WebDriverEvent event, String engine) {
	}

	@Override
	public void beforeDismiss(WebDriverEvent event) {
	}

	@Override
	public void afterDismiss(WebDriverEvent event) {
	}

	@Override
	public void beforeAccept(WebDriverEvent event) {
	}

	@Override
	public void afterAccept(WebDriverEvent event) {
	}

	@Override
	public void beforeGetTextByAlert(WebDriverEvent event) {
	}

	@Override
	public void afterGetTextByAlert(WebDriverEvent event, String text) {
	}

	@Override
	public void beforeSendKeysByAlert(WebDriverEvent event, String keysToSend) {
	}

	@Override
	public void afterSendKeysByAlert(WebDriverEvent event, String keysToSend) {
	}

	@Override
	public <T extends WebElement> void beforeGetCoordinates(WebDriverEvent event, T element) {
	}

	@Override
	public <T extends WebElement> void afterGetCoordinates(WebDriverEvent event, Coordinates coordinates, T element) {
	}

	@Override
	public <X, T extends WebElement> void beforeGetScreenshotAsByElement(WebDriverEvent event, OutputType<X> target, T element) {
	}

	@Override
	public <X, T extends WebElement> void afterGetScreenshotAsByElement(WebDriverEvent event, OutputType<X> target, X screenshot, T element) {
	}

	@Override
	public <T extends WebElement> void beforeUploadFile(WebDriverEvent event, T element, File localFile) {
	}

	@Override
	public <T extends WebElement> void afterUploadFile(WebDriverEvent event, T element, File localFile, String response) {
	}

	@JsonProperty("logEntries")
	@Override
	public List<WebDriverEvent> getListOfEventsRecorded() {
		return Collections.unmodifiableList(logEntries);
	}
	
	@Override
	public String getEventsFormatted() {
		return null;
	}

	protected TestEvent createTestEvent(WebDriverEvent event, Level eventLevel){
		String param1 = event.getParam1() == null ? "" : event.getParam1();
		String param2 = event.getParam2() == null ? "" : event.getParam2();
		String cmd = event.getCmd().getLongCmdString();
		cmd = cmd == null ? "" : cmd;
		String locator = event.getElementLocator();
		locator = locator == null ? "" : locator;
        return new TestEvent(event.toString(), 
							eventLevel.toString(),
							cmd, 
							param1 + param2, 
							locator,
							event.getRecordNumber(), 
							null);
	}
}
