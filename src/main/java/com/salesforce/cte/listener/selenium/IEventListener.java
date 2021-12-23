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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.print.PrintOptions;

import com.salesforce.cte.listener.selenium.WebDriverEvent.Cmd;

/**
 * Interface which supports registering of a listener with {@link com.salesforce.cte.listener.selenium.EventDispatcher} for logging
 * purposes.
 * 
 * This is an extended version of org.openqa.selenium.support.events.WebDriverEventListener. See
 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/events/WebDriverEventListener.html
 * for more information.
 * 
 * @since 1.0
 */
public interface IEventListener {
	/**
	 * Location of logfiles produced by Test Drop-in Framework and
	 * its dependent classes: {@value}
	 */
	final String TEST_ADVISOR_LOGFILES_DIR = "target/";

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#close close()}.
	 * @param event
	 *            event record
	 */
	void beforeClose(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#close close()}.
	 * @param event
	 *            event record
	 */
	void afterClose(WebDriverEvent event);

	/**
	 * Called before {@link WebDriver#findElement WebDriver.findElement(...)}.
	 * @param event
	 *            event record
	 * @param by
	 *            locator being used
	 */
	void beforeFindElement(WebDriverEvent event, By by);

	/**
	 * Called after {@link WebDriver#findElement WebDriver.findElement(...)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            returned element
	 * @param by
	 *            locator being used
	 */
	<T extends WebElement> void afterFindElement(WebDriverEvent event, T element, By by);

	/**
	 * Called before {@link WebDriver#findElements WebDriver.findElements(...)}.
	 * @param event
	 *            event record
	 * @param by
	 *            locator being used
	 */
	void beforeFindElements(WebDriverEvent event, By by);

	/**
	 * Called after{@link WebDriver#findElements WebDriver.findElements(..)}.
	 * 
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param elements
	 *            returned list of elements
	 * @param by
	 *            locator being used
	 */
	<T extends WebElement> void afterFindElements(WebDriverEvent event, List<T> elements, By by);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#get get(String url)}.
	 *
	 * @param event
	 *            event record
	 * @param url
	 *            URL
	 */
	void beforeGet(WebDriverEvent event, String url);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#get get(String url)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param url
	 *            URL
	 */
	void afterGet(WebDriverEvent event, String url);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getCurrentUrl getCurrentUrl()}.
	 * @param event
	 *            event record
	 */
	void beforeGetCurrentUrl(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getCurrentUrl getCurrentUrl()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param event
	 *            event record
	 * @param url
	 *            returned URL
	 */
	void afterGetCurrentUrl(WebDriverEvent event, String url);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getPageSource getPageSource()}.
	 * @param event
	 *            event record
	 */
	void beforeGetPageSource(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getPageSource getPageSource()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param event
	 *            event record
	 * @param source
	 *            returned page source
	 */
	void afterGetPageSource(WebDriverEvent event, String source);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getTitle getTitle()}.
	 * @param event
	 *            event record
	 */
	void beforeGetTitle(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getTitle getTitle()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param event
	 *            event record
	 * @param title
	 *            returned page title
	 */
	void afterGetTitle(WebDriverEvent event, String title);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getWindowHandle getWindowHandle()}.
	 * @param event
	 *            event record
	 */
	void beforeGetWindowHandle(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getWindowHandle getWindowHandle()}.
	 *
	 * @param event
	 *            event record
	 * @param handle
	 *            Handle to current window
	 */
	void afterGetWindowHandle(WebDriverEvent event, String handle);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getWindowHandles getWindowHandles()}.
	 * @param event
	 *            event record
	 */
	void beforeGetWindowHandles(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getWindowHandles getWindowHandles()}.
	 * @param event
	 *            event record
	 * @param handles
	 *            Set of handles to windows currently open
	 */
	void afterGetWindowHandles(WebDriverEvent event, Set<String> handles);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#quit quit()}.
	 * @param event
	 *            event record
	 */
	void beforeQuit(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#quit quit()}.
	 * @param event
	 *            event record
	 */
	void afterQuit(WebDriverEvent event);

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.JavascriptExecutor#executeAsyncScript(String, Object...) executingAsyncScript(String, Object...)}.
	 * @param event
	 *            event record
	 * @param script
	 *            JavaScript script to execute
	 * @param convertedArgs
	 *            arguments for script
	 */
	void beforeExecuteAsyncScript(WebDriverEvent event, String script, List<Object> convertedArgs);

	/**
	 * Called after {@link org.openqa.selenium.JavascriptExecutor#executeAsyncScript(String, Object...) executingAsyncScript(String, Object...)}.
	 * @param event
	 *            event record
	 * @param script
	 *            JavaScript script executed
	 * @param convertedArgs
	 *            arguments for script
	 * @param result
	 *            returned object
	 */
	void afterExecuteAsyncScript(WebDriverEvent event, String script, List<Object> convertedArgs, Object result);

	/**
	 * Called before {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) executeScript(String, Object...)}.
	 * @param event
	 *            event record
	 * @param script
	 *            JavaScript script to execute
	 * @param convertedArgs
	 *            arguments for script
	 */
	void beforeExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs);

	/**
	 * Called after {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) executeScript(String, Object...)}.
	 * @param event
	 *            event record
	 * @param script
	 *            JavaScript script executed
	 * @param convertedArgs
	 *            arguments for script
	 * @param result
	 *            returned object
	 */
	void afterExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs, Object result);

	/**
	 * Called before {@link org.openqa.selenium.remote.RemoteWebDriver#perform perform(..)}.
	 * @param event
	 *            event record
	 * @param actions
	 *            sequence of actions
	 */
	void beforeActions(WebDriverEvent event, Collection<Sequence> actions);

	/**
	 * Called after {@link org.openqa.selenium.remote.RemoteWebDriver#perform perform(..)}.
	 * @param event
	 *            event record
	 * @param actions
	 *            sequence of actions
	 */
	void afterActions(WebDriverEvent event, Collection<Sequence> actions);

	/**
	 * Called before {@link org.openqa.selenium.remote.RemoteWebDriver#print print(..)}.
	 * @param event
	 *            event record
	 * @param printOptions
	 *            printing options
	 */
	void beforePrint(WebDriverEvent event, PrintOptions printOptions);

	/**
	 * Called after {@link org.openqa.selenium.remote.RemoteWebDriver#print print(..)}.
	 * @param event
	 *            event record
	 * @param printOptions
	 *            printing options
	 * @param printedPdfPage
	 *            printed page in PDF format
	 */
	void afterPrint(WebDriverEvent event, PrintOptions printOptions, Pdf printedPdfPage);

	/**
	 * Called before {@link org.openqa.selenium.remote.RemoteWebDriver#resetInputState resetInputState()}.
	 * @param event
	 *            event record
	 */
	void beforeResetInputState(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.remote.RemoteWebDriver#resetInputState resetInputState()}.
	 * @param event
	 *            event record
	 */
	void afterResetInputState(WebDriverEvent event);

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Options object
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Options#addCookie(Cookie) addCookie(Cookie cookie)}.
	 * 
	 * @param event
	 *            event record
	 * @param cookie
	 *            cookie to add
	 */
	void beforeAddCookie(WebDriverEvent event, Cookie cookie);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Options#addCookie(Cookie) addCookie(Cookie cookie)}.
	 * @param event
	 *            event record
	 * @param cookie
	 *            cookie to add
	 */
	void afterAddCookie(WebDriverEvent event, Cookie cookie);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Options#deleteCookieNamed(String) deleteCookieNamed(String name)}.
	 * 
	 * @param event
	 *            event record
	 * @param name
	 *            name of cookie to delete
	 */
	void beforeDeleteCookieNamed(WebDriverEvent event, String name);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Options#deleteCookieNamed(String) deleteCookieNamed(String name)}.
	 * @param event
	 *            event record
	 * @param name
	 *            name of cookie to delete
	 */
	void afterDeleteCookieNamed(WebDriverEvent event, String name);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Options#deleteCookie(Cookie) deleteCookie(Cookie cookie)}.
	 * 
	 * @param event
	 *            event record
	 * @param cookie
	 *            cookie to delete
	 */
	void beforeDeleteCookie(WebDriverEvent event, Cookie cookie);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Options#deleteCookie(Cookie) deleteCookie(Cookie cookie)}.
	 * @param event
	 *            event record
	 * @param cookie
	 *            cookie to delete
	 */
	void afterDeleteCookie(WebDriverEvent event, Cookie cookie);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Options#deleteAllCookies() deleteAllCookies()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeDeleteAllCookies(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Options#deleteAllCookies() deleteAllCookies()}.
	 * @param event
	 *            event record
	 */
	void afterDeleteAllCookies(WebDriverEvent event);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Options#getCookies() getCookies()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeGetCookies(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Options#getCookies() getCookies()}.
	 * @param event
	 *            event record
	 * @param cookies
	 *            set of all cookies
	 */
	void afterGetCookies(WebDriverEvent event, Set<Cookie> cookies);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Options#addCookie(Cookie) getCookieNamed(String name)}.
	 * 
	 * @param event
	 *            event record
	 * @param name
	 *            name of cookie to get
	 */
	void beforeGetCookieNamed(WebDriverEvent event, String name);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Options#getCookieNamed(String) getCookieNamed(String name)}.
	 * @param event
	 *            event record
	 * @param name
	 *            name of cookie to get
	 * @param cookie
	 *            returned cookie
	 */
	void afterGetCookieNamed(WebDriverEvent event, String name, Cookie cookie);
	
	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to TakesScreenshot.
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.TakesScreenshot#getScreenshotAs(OutputType target) getScreenshotAs(OutputType&lt;X&gt; target)}.
	 * 
	 * @param <X> 
	 *            Return type for getScreenshotAs.
	 * @param event
	 *            event record
	 * @param target
	 *            target type, @see OutputType
	 */
	<X> void beforeGetScreenshotAs(WebDriverEvent event, OutputType<X> target);

	/**
	 * Called after {@link org.openqa.selenium.TakesScreenshot#getScreenshotAs(OutputType target) getScreenshotAs(OutputType&lt;X&gt; target)}.
	 * 
	 * @param <X> 
	 *            Return type for getScreenshotAs.
	 * @param event
	 *            event record
	 * @param target
	 *            target type, @see OutputType
	 * @param screenshot
	 *            screenshot captured
	 */
	<X> void afterGetScreenshotAs(WebDriverEvent event, OutputType<X> target, X screenshot);

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.ImeHandler object
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.ImeHandler#getAvailableEngines() getAvailableEngines()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeGetAvailableEngines(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.ImeHandler#getAvailableEngines() getAvailableEngines()}.
	 * 
	 * @param event
	 *            event record
	 * @param engines
	 *            list of names of available engines
	 */
	void afterGetAvailableEngines(WebDriverEvent event, List<String> engines);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.ImeHandler#getActiveEngine() getActiveEngine()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeGetActiveEngine(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.ImeHandler#getActiveEngine() getActiveEngine()}.
	 * 
	 * @param event
	 *            event record
	 * @param engine
	 *            names of active engine
	 */
	void afterGetActiveEngine(WebDriverEvent event, String engine);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.ImeHandler#isActivated() isActivated()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeIsActivated(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.ImeHandler#isActivated() isActivated()}.
	 * 
	 * @param event
	 *            event record
	 * @param isActive
	 *            state of activation of current engine
	 */
	void afterIsActivated(WebDriverEvent event, boolean isActive);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.ImeHandler#deactivate() deactivate()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeDeactivate(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.ImeHandler#deactivate() deactivate()}.
	 * 
	 * @param event
	 *            event record
	 */
	void afterDeactivate(WebDriverEvent event);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.ImeHandler#activateEngine(String) activateEngine(String engine)}.
	 * 
	 * @param event
	 *            event record
	 * @param engine
	 *            name of engine to activate
	 */
	void beforeActivateEngine(WebDriverEvent event, String engine);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.ImeHandler#activateEngine(String) activateEngine(String engine)}.
	 * 
	 * @param event
	 *            event record
	 * @param engine
	 *            name of engine to activate
	 */
	void afterActivateEngine(WebDriverEvent event, String engine);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#back
	 * navigate().back()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeBack(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation
	 * navigate().back()}. Not called, if an exception is thrown.
	 * 
	 * @param event
	 *            event record
	 */
	void afterBack(WebDriverEvent event);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeForward(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}. Not called, if an exception is thrown.
	 * 
	 * @param event
	 *            event record
	 */
	void afterForward(WebDriverEvent event);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeRefresh(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}. Not called, if an exception is thrown.
	 * 
	 * @param event
	 *            event record
	 */
	void afterRefresh(WebDriverEvent event);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Alert object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.Alert#dismiss switchTo().alert().dismiss()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeDismiss(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.Alert#dismiss switchTo().alert().dismiss()}.
	 * Not called, if an exception is thrown.
	 * 
	 * @param event
	 *            event record
	 */
	void afterDismiss(WebDriverEvent event);

	/**
	 * Called before {@link org.openqa.selenium.Alert#accept switchTo().alert().accept()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeAccept(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.Alert#accept switchTo().alert().accept()}. 
	 * Not called, if an exception is thrown.
	 * 
	 * @param event
	 *            event record
	 */
	void afterAccept(WebDriverEvent event);

	/**
	 * Called before {@link org.openqa.selenium.Alert#getText switchTo().alert().getText()}.
	 * 
	 * @param event
	 *            event record
	 */
	void beforeGetTextByAlert(WebDriverEvent event);

	/**
	 * Called after {@link org.openqa.selenium.Alert#getText switchTo().alert().getText()}.
	 * Not called, if an exception is thrown.
	 * 
	 * @param event
	 *            event record
	 * @param text
	 *            text shown in alert
	 */
	void afterGetTextByAlert(WebDriverEvent event, String text);

	/**
	 * Called before {@link org.openqa.selenium.Alert#sendKeys(String) switchTo().alert().sendKeys(String keysToSend)}.
	 * 
	 * @param event
	 *            event record
	 * @param keysToSend
	 *            keys to enter
	 */
	void beforeSendKeysByAlert(WebDriverEvent event, String keysToSend);

	/**
	 * Called after {@link org.openqa.selenium.Alert#sendKeys(String) switchTo().alert().sendKeys(String keysToSend)}.
	 * Not called, if an exception is thrown.
	 * 
	 * @param event
	 *            event record
	 * @param keysToSend
	 *            keys to enter
	 */
	void afterSendKeysByAlert(WebDriverEvent event, String keysToSend);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.TargetLocator#activeElement() TargetLocator.activeElement()}.
	 * @param event
	 *            event record
	 */
	void beforeActiveElement(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.TargetLocator#activeElement() TargetLocator.activeElement()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event event record
	 * @param activeElement the current active WebElement
	 */
	<T extends WebElement> void afterActiveElement(WebDriverEvent event, T activeElement);

	/**
	 * Called before {@link WebDriver.TargetLocator#defaultContent() TargetLocator.defaultContent()}.
	 * @param event
	 *            event record
	 */
	void beforeDefaultContent(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.TargetLocator#defaultContent() TargetLocator.defaultContent()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 */
	void afterDefaultContent(WebDriverEvent event);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 * @param event
	 *            event record
	 * @param frameIndex
	 *            0-based index of frame on page
	 */
	void beforeFrameByIndex(WebDriverEvent event, int frameIndex);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param frameIndex
	 *            0-based index of frame on page
	 */
	void afterFrameByIndex(WebDriverEvent event, int frameIndex);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(WebElement) TargetLocator.frame(webElement)}
	 * or {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(name)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param frameElement
	 *            element inside frame
	 */
	<T extends WebElement> void beforeFrameByElement(WebDriverEvent event, T frameElement);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(WebElement) TargetLocator.frame(webElement)}
	 * or {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(name)}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param frameElement
	 *            element inside frame
	 */
	<T extends WebElement> void afterFrameByElement(WebDriverEvent event, T frameElement);

	/**
	 * Called before {@link WebDriver.TargetLocator#parentFrame() TargetLocator.parentFrame()}.
	 * @param event
	 *            event record
	 */
	void beforeParentFrame(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.TargetLocator#parentFrame() TargetLocator.parentFrame()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 */
	void afterParentFrame(WebDriverEvent event);

	/**
	 * Called before {@link WebDriver.TargetLocator#window(java.lang.String) TargetLocator.window(..)}.
	 * @param event
	 *            event record
	 * @param windowName
	 *            name of window
	 */
	void beforeWindow(WebDriverEvent event, String windowName);

	/**
	 * Called after {@link WebDriver.TargetLocator#window(java.lang.String) TargetLocator.window(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param windowName
	 *            name of window
	 */
	void afterWindow(WebDriverEvent event, String windowName);

	/**
	 * Called before {@link WebDriver.TargetLocator#newWindow(WindowType) TargetLocator.newWindow(..)}.
	 * @param event
	 *            event record
	 * @param typeHint
	 *            type of new window (browser tab or browser window)
	 */
	void beforeNewWindow(WebDriverEvent event, WindowType typeHint);

	/**
	 * Called after {@link WebDriver.TargetLocator#newWindow(WindowType) TargetLocator.newWindow(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param typeHint
	 *            type of new window (browser tab or browser window)
	 */
	void afterNewWindow(WebDriverEvent event, WindowType typeHint);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Timeouts object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.Timeouts#implicitlyWait(java.time.Duration) Timeouts.implicitlyWait(..)}.
	 * @param event
	 *            event record
	 * @param duration
	 *            time to wait
	 */
	void beforeImplicitlyWait(WebDriverEvent event, Duration duration);

	/**
	 * Called after {@link WebDriver.Timeouts#implicitlyWait(java.time.Duration) Timeouts.implicitlyWait(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param duration
	 *            time to wait
	 */
	void afterImplicitlyWait(WebDriverEvent event, Duration duration);

	/**
	 * Called before {@link WebDriver.Timeouts#getImplicitWaitTimeout() Timeouts.getImplicitWaitTimeout()}.
	 * @param event
	 *            event record
	 */
	void beforeGetImplicitWaitTimeout(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Timeouts#getImplicitWaitTimeout() Timeouts.getImplicitWaitTimeout()}.
	 * @param event
	 *            event record
	 * @param duration
	 *            timeout currently in use
	 */
	void afterGetImplicitWaitTimeout(WebDriverEvent event, Duration duration);

	/**
	 * Called before {@link WebDriver.Timeouts#pageLoadTimeout(java.time.Duration) Timeouts.pageLoadTimeout(..)}.
	 * @param event
	 *            event record
	 * @param duration
	 *            time to wait
	 */
	void beforePageLoadTimeout(WebDriverEvent event, Duration duration);

	/**
	 * Called after {@link WebDriver.Timeouts#pageLoadTimeout(java.time.Duration) Timeouts.pageLoadTimeout(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param duration
	 *            time to wait
	 */
	void afterPageLoadTimeout(WebDriverEvent event, Duration duration);

	/**
	 * Called before {@link WebDriver.Timeouts#getPageLoadTimeout() Timeouts.getPageLoadTimeout()}.
	 * @param event
	 *            event record
	 */
	void beforeGetPageLoadTimeout(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Timeouts#getPageLoadTimeout() Timeouts.getPageLoadTimeout()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param duration
	 *            time to wait
	 */
	void afterGetPageLoadTimeout(WebDriverEvent event, Duration duration);

	/**
	 * Called before {@link WebDriver.Timeouts#setScriptTimeout(java.time.Duration) Timeouts.setScriptTimeout(..)}.
	 * @param event
	 *            event record
	 * @param duration
	 *            time to wait
	 */
	void beforeSetScriptTimeout(WebDriverEvent event, Duration duration);

	/**
	 * Called after {@link WebDriver.Timeouts#setScriptTimeout(java.time.Duration) Timeouts.setScriptTimeout(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param duration
	 *            time to wait
	 */
	void afterSetScriptTimeout(WebDriverEvent event, Duration duration);

	/**
	 * Called before {@link WebDriver.Timeouts#getScriptTimeout() Timeouts.getScriptTimeout()}.
	 * @param event
	 *            event record
	 */
	void beforeGetScriptTimeout(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Timeouts#getScriptTimeout() Timeouts.getScriptTimeout()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param duration
	 *            timeout currently set for script execution
	 */
	void afterGetScriptTimeout(WebDriverEvent event, Duration duration);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.Window#fullscreen() Window.fullscreen()}.
	 * @param event
	 *            event record
	 */
	void beforeFullscreen(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Window#fullscreen() Window.fullscreen()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 */
	void afterFullscreen(WebDriverEvent event);

	/**
	 * Called before {@link WebDriver.Window#getPosition() getPosition()}.
	 * @param event
	 *            event record
	 */
	void beforeGetPosition(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Window#getPosition() getPosition()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param targetPosition
	 *            returned location on screen
	 */
	void afterGetPosition(WebDriverEvent event, Point targetPosition);

	/**
	 * Called before {@link WebDriver.Window#getSize() getSize()}.
	 * @param event
	 *            event record
	 */
	void beforeGetSizeByWindow(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Window#getSize() getSize()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param targetSize
	 *            returned window size on screen
	 */
	void afterGetSizeByWindow(WebDriverEvent event, Dimension targetSize);

	/**
	 * Called before {@link WebDriver.Window#maximize() Window.maximize()}.
	 * @param event
	 *            event record
	 */
	void beforeMaximize(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Window#maximize() Window.maximize()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 */
	void afterMaximize(WebDriverEvent event);

	/**
	 * Called before {@link WebDriver.Window#minimize() Window.minimize()}.
	 * @param event
	 *            event record
	 */
	void beforeMinimize(WebDriverEvent event);

	/**
	 * Called after {@link WebDriver.Window#minimize() Window.minimize()}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 */
	void afterMinimize(WebDriverEvent event);

	/**
	 * Called before {@link WebDriver.Window#setPosition(Point) setPosition(..)}.
	 * @param event
	 *            event record
	 * @param targetPosition
	 *            location on screen
	 */
	void beforeSetPosition(WebDriverEvent event, Point targetPosition);

	/**
	 * Called after {@link WebDriver.Window#setPosition(Point) setPosition(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param targetPosition
	 *            location on screen
	 */
	void afterSetPosition(WebDriverEvent event, Point targetPosition);

	/**
	 * Called before {@link WebDriver.Window#setSize(Dimension) setSize(..)}.
	 * @param event
	 *            event record
	 * @param targetSize
	 *            window size on screen
	 */
	void beforeSetSizeByWindow(WebDriverEvent event, Dimension targetSize);

	/**
	 * Called after {@link WebDriver.Window#setSize(Dimension) setSize(..)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param targetSize
	 *            window size on screen
	 */
	void afterSetSizeByWindow(WebDriverEvent event, Dimension targetSize);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebElement#click WebElement.click()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeClick(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#click WebElement.click()}. Not called, if an
	 * exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterClick(WebDriverEvent event, T element);

	/**
	 * Called before {@link WebElement#clear WebElement.clear()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeClear(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#clear WebElement.clear()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterClear(WebDriverEvent event, T element);

	/**
	 * Called before {@link WebElement#getAttribute WebElement.getAttribute(...)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param name
	 *            name of the attribute to get
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetAttribute(WebDriverEvent event, String name, T element);

	/**
	 * Called after {@link WebElement#getAttribute WebElement.getAttribute(...)}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param value
	 *            value of the named attribute
	 * @param name
	 *            name of the attribute to get
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetAttribute(WebDriverEvent event, String value, String name, T element);

	/**
	 * Called before {@link WebElement#getCssValue WebElement.getCssValue()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param propertyName
	 * 			  name of the CSS property to get the value of 
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetCssValue(WebDriverEvent event, String propertyName, T element);

	/**
	 * Called after {@link WebElement#getCssValue WebElement.getCssValue()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param propertyName
	 * 			  name of the CSS property to get the value of 
	 * @param value
	 *            the retrieved CSS value
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetCssValue(WebDriverEvent event, String propertyName, String value, T element);

	/**
	 * Called before {@link WebElement#getDomProperty WebElement.getDomProperty(..)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param name
	 *            property name
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetDomProperty(WebDriverEvent event, String name, T element);

	/**
	 * Called after {@link WebElement#getDomProperty WebElement.getDomProperty(..)}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param name
	 *            property name
	 * @param value
	 *            the retrieved DOM property value
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetDomProperty(WebDriverEvent event, String name, String value, T element);

	/**
	 * Called before {@link WebElement#getDomAttribute WebElement.getDomAttribute(..)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param name
	 *            attribute name
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetDomAttribute(WebDriverEvent event, String name, T element);

	/**
	 * Called after {@link WebElement#getDomAttribute WebElement.getDomAttribute(..)}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param name
	 *            attribute name
	 * @param value
	 *            the retrieved DOM attribute value
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetDomAttribute(WebDriverEvent event, String name, String value, T element);

	/**
	 * Called before {@link WebElement#getTagName WebElement.getTagName()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetTagName(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getTagName WebElement.getTagName()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param tagName
	 *            the retrieved tag name
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetTagName(WebDriverEvent event, String tagName, T element);

	/**
	 * Called before {@link WebElement#getAriaRole WebElement.getAriaRole()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetAriaRole(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getAriaRole WebElement.getAriaRole()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param role
	 *            the retrieved Aria role
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetAriaRole(WebDriverEvent event, String role, T element);

	/**
	 * Called before {@link WebElement#getAccessibleName WebElement.getAccessibleName()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetAccessibleName(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getAccessibleName WebElement.getAccessibleName()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param name
	 *            the retrieved accessible name
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetAccessibleName(WebDriverEvent event, String name, T element);

	/**
	 * Called before {@link WebElement#getText WebElement.getText()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetText(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getText WebElement.getText()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param text
	 *            the retrieved text
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetText(WebDriverEvent event, String text, T element);

	/**
	 * Called before {@link WebElement#isDisplayed WebElement.isDisplayed()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeIsDisplayed(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#isDisplayed WebElement.isDisplayed()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param isDisplayed
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterIsDisplayed(WebDriverEvent event, boolean isDisplayed, T element);

	/**
	 * Called before {@link WebElement#isEnabled WebElement.isEnabled()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeIsEnabled(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#isEnabled WebElement.isEnabled()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param isEnabled
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterIsEnabled(WebDriverEvent event, boolean isEnabled, T element);

	/**
	 * Called before {@link WebElement#isSelected WebElement.isSelected()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeIsSelected(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#isSelected WebElement.isSelected()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param isSelected
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterIsSelected(WebDriverEvent event, boolean isSelected, T element);
	
	/**
	 * Called before {@link WebElement#getLocation WebElement.getLocation()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetLocation(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getLocation WebElement.getLocation()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param point
	 *            the retrieved point
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetLocation(WebDriverEvent event, Point point, T element);
	
	/**
	 * Called before {@link WebElement#getSize WebElement.getSize()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetSizeByElement(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getSize WebElement.getSize()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param dimension
	 *            the retrieved dimension
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetSizeByElement(WebDriverEvent event, Dimension dimension, T element);

	/**
	 * Called before {@link WebElement#getRect WebElement.getRect()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetRect(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getRect WebElement.getRect()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param rectangle
	 *            the retrieved rectangle
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetRect(WebDriverEvent event, Rectangle rectangle, T element);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Locatable#getCoordinates getCoordinates()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetCoordinates(WebDriverEvent event, T element);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Locatable#getCoordinates getCoordinates()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param coordinates
	 *            the retrieved coordinates
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetCoordinates(WebDriverEvent event, Coordinates coordinates, T element);

	/**
	 * Called before {@link org.openqa.selenium.TakesScreenshot#getScreenshotAs(OutputType target) getScreenshotAs(OutputType&lt;X&gt; target)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param <X>
	 *            Return type for getScreenshotAs.
	 * @param event
	 *            event record
	 * @param target
	 *            target type, @see OutputType
	 * @param element
	 *            the WebElement being used for the action
	 */
	<X, T extends WebElement> void beforeGetScreenshotAsByElement(WebDriverEvent event, OutputType<X> target, T element);

	/**
	 * Called after {@link org.openqa.selenium.TakesScreenshot#getScreenshotAs(OutputType target) getScreenshotAs(OutputType&lt;X&gt; target)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param <X>
	 *            Return type for getScreenshotAs.
	 * @param event
	 *            event record
	 * @param target
	 *            target type, @see OutputType
	 * @param screenshot
	 *            screenshot captured
	 * @param element
	 *            the WebElement being used for the action
	 */
	<X, T extends WebElement> void afterGetScreenshotAsByElement(WebDriverEvent event, OutputType<X> target, X screenshot, T element);

	/**
	 * Called before {@link WebElement#sendKeys WebElement.sendKeys(...)}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 * @param keysToSend
	 *            text to insert
	 */
	<T extends WebElement> void beforeSendKeysByElement(WebDriverEvent event, T element, CharSequence... keysToSend);

	/**
	 * Called after {@link WebElement#sendKeys WebElement.sendKeys(...)}}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 * @param keysToSend
	 *            text to insert
	 */
	<T extends WebElement> void afterSendKeysByElement(WebDriverEvent event, T element, CharSequence... keysToSend);

	/**
	 * Called before {@link WebElement#sendKeys WebElement.sendKeys(...)} if the keys to send
	 * are the name of a local file.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 * @param localFile
	 *            local file to upload
	 */
	<T extends WebElement> void beforeUploadFile(WebDriverEvent event, T element, File localFile);

	/**
	 * Called after {@link WebElement#sendKeys WebElement.sendKeys(...)}} if the keys to send
	 * are the name of a local file. Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 * @param localFile
	 *            local file to upload
	 * @param response
	 *            response to file upload
	 */
	<T extends WebElement> void afterUploadFile(WebDriverEvent event, T element, File localFile, String response);
	
	/**
	 * Called before {@link WebElement#submit WebElement.submit()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeSubmit(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#submit WebElement.submit()}. Not called, if an
	 * exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterSubmit(WebDriverEvent event, T element);
	
	/**
	 * Called before {@link WebElement#getShadowRoot WebElement.getShadowRoot()}.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void beforeGetShadowRoot(WebDriverEvent event, T element);

	/**
	 * Called after {@link WebElement#getShadowRoot WebElement.getShadowRoot()}.
	 * Not called, if an exception is thrown.
	 * @param <T> WebElement or any type extending WebElement to support customized
	 *            WebElement classes
	 * @param event
	 *            event record
	 * @param element
	 *            the WebElement being used for the action
	 */
	<T extends WebElement> void afterGetShadowRoot(WebDriverEvent event, T element);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Keyboard#sendKeys Keyboard.sendKeys(...)}.
	 * @param event
	 *            event record
	 * @param keysToSend
	 *            text to insert
	 */
	void beforeSendKeysByKeyboard(WebDriverEvent event, CharSequence... keysToSend);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Keyboard#sendKeys Keyboard.sendKeys(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param keysToSend
	 *            text to insert
	 */
	void afterSendKeysByKeyboard(WebDriverEvent event, CharSequence... keysToSend);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Keyboard#pressKey Keyboard.pressKey(...)}.
	 * @param event
	 *            event record
	 * @param keyToPress
	 *            key to press
	 */
	void beforePressKey(WebDriverEvent event, CharSequence... keyToPress);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Keyboard#pressKey Keyboard.pressKey(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param keyToPress
	 *            key to press
	 */
	void afterPressKey(WebDriverEvent event, CharSequence... keyToPress);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Keyboard#releaseKey Keyboard.releaseKey(...)}.
	 * @param event
	 *            event record
	 * @param keyToRelease
	 *            key to release
	 */
	void beforeReleaseKey(WebDriverEvent event, CharSequence... keyToRelease);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Keyboard#releaseKey Keyboard.releaseKey(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param keyToRelease
	 *            key to release
	 */
	void afterReleaseKey(WebDriverEvent event, CharSequence... keyToRelease);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#click Mouse.click(...)}.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where click is performed
	 */
	void beforeClickByMouse(WebDriverEvent event, Coordinates where);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Mouse#click Mouse.click(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where click is performed
	 */
	void afterClickByMouse(WebDriverEvent event, Coordinates where);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#doubleClick Mouse.doubleClick(...)}.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where double click is performed
	 */
	void beforeDoubleClick(WebDriverEvent event, Coordinates where);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Mouse#doubleClick Mouse.doubleClick(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where double click is performed
	 */
	void afterDoubleClick(WebDriverEvent event, Coordinates where);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#mouseDown Mouse.mouseDown(...)}.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse down is performed
	 */
	void beforeMouseDown(WebDriverEvent event, Coordinates where);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Mouse#mouseDown Mouse.mouseDown(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse down is performed
	 */
	void afterMouseDown(WebDriverEvent event, Coordinates where);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#mouseUp Mouse.mouseUp(...)}.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse up is performed
	 */
	void beforeMouseUp(WebDriverEvent event, Coordinates where);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Mouse#mouseUp Mouse.mouseUp(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse up is performed
	 */
	void afterMouseUp(WebDriverEvent event, Coordinates where);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#mouseMove(Coordinates) Mouse.mouseMove(Coordinates where)}.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse is moved to
	 */
	void beforeMouseMove(WebDriverEvent event, Coordinates where);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Mouse#mouseMove(Coordinates) Mouse.mouseMove(Coordinates where)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse is moved to
	 */
	void afterMouseMove(WebDriverEvent event, Coordinates where);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#mouseMove(Coordinates,long,long) Mouse.mouseMove(Coordinates where, longxOffset, long yOffset)}.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse is moved to
	 * @param xOffset
	 *            offset in x direction
	 * @param yOffset
	 *            offset in y direction
	 */
	void beforeMouseMove(WebDriverEvent event, Coordinates where, long xOffset, long yOffset);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#mouseMove(Coordinates,long,long) Mouse.mouseMove(Coordinates where, longxOffset, long yOffset)}.
	 * Not called, if an exception is thrown.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where mouse is moved to
	 * @param xOffset
	 *            offset in x direction
	 * @param yOffset
	 *            offset in y direction
	 */
	void afterMouseMove(WebDriverEvent event, Coordinates where, long xOffset, long yOffset);

	/**
	 * Called before {@link org.openqa.selenium.interactions.Mouse#contextClick Mouse.contextClick(...)}.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where context click is performed
	 */
	void beforeContextClick(WebDriverEvent event, Coordinates where);

	/**
	 * Called after {@link org.openqa.selenium.interactions.Mouse#contextClick Mouse.contextClick(...)}}. Not called, if an
	 * exception is thrown.
	 * @param event
	 *            event record
	 * @param where
	 *            coordinates where context click is performed
	 */
	void afterContextClick(WebDriverEvent event, Coordinates where);

	/**
	 * Called whenever a command throws an exception.
	 * @param event
	 *            event record
	 * @param cmd
	 *            the command which ran into an issue
	 * @param throwable
	 *            the exception that will be thrown
	 */
	void onException(WebDriverEvent event, Cmd cmd, Throwable throwable);
	
	/**
	 * Gets the list of events logged so far by the implementing listener.
	 * 
	 * Preferably it should return an immutable list by wrapping the
	 * list-to-be-returned with {@link Collections#unmodifiableList(List)} so that
	 * multiple calls of this method are allowed.
	 * 
	 * @return list of events logged so far
	 */
	List<WebDriverEvent> getListOfEventsRecorded();
	
	/**
	 * Gets the events logged so far by the implementing listener as a string
	 * or NULL, if this makes no sense.
	 * 
	 * The event listener can present the logged events in a formatted style,
	 * ready to written to disk as-is or deny provisioning this output by
	 * returning NULL.
	 * 
	 * @return events logged so far as string or NULL
	 */
	String getEventsFormatted();
}
