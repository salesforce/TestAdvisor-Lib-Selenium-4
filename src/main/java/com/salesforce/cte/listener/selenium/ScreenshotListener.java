/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.cte.listener.selenium;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.salesforce.cte.admin.TestAdvisorConfiguration;
import com.salesforce.cte.common.TestEvent;
import com.salesforce.cte.common.TestEventType;

import org.openqa.selenium.remote.RemoteWebDriver;

public class ScreenshotListener extends AbstractEventListener {
    private TakesScreenshot tss;
	private RemoteWebDriver rwd;

	@Override
	public void setWebDriver(WebDriver driver) {
		if (driver instanceof RemoteWebDriver) {
			List<Method> methods = Arrays.stream(RemoteWebDriver.class.getMethods()).filter(m -> m.getName().equals("getScreenshotAsForTestAdvisor")).
					collect(Collectors.toList());
			if (methods.isEmpty())
				this.tss = (TakesScreenshot) driver;
			else
				this.rwd = (RemoteWebDriver) driver;
		}
	}
	
    /*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/
    @Override
	public void beforeClose(WebDriverEvent event) {
        captureScreenShot(event); 
	}

	@Override
	public void beforeGet(WebDriverEvent event, String url) {
		captureScreenShot(event);
	}

    /*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

    @Override
	public void beforeExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs) {
        if (script.contains("click")){
            captureScreenShot(event);
        }
	}

    /*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/
    @Override
	public void beforeBack(WebDriverEvent event) {
        captureScreenShot(event);
	}

	@Override
	public void beforeForward(WebDriverEvent event) {
        captureScreenShot(event);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeClick(WebDriverEvent event, WebElement element) {
        captureScreenShot(event);   
	}

	@Override
	public void beforeClear(WebDriverEvent event, WebElement element) {
        captureScreenShot(event);   
	}

	@Override
	public void beforeSendKeysByElement(WebDriverEvent event, WebElement element, CharSequence... keysToSend) {
		// Skip capturing a screenshot if it is the same locator, because it means
		// a test is sending text character by character to the same text field.
		if (isDifferentLocator(element))
			captureScreenShot(event);
	}

	@Override
	public void beforeSubmit(WebDriverEvent event, WebElement element) {
        captureScreenShot(event);   
    }

    /*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Alert object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeDismiss(WebDriverEvent event) {
        captureScreenShot(event);
	}

	@Override
	public void beforeAccept(WebDriverEvent event) {
        captureScreenShot(event);
	}

	@Override
	public void beforeSendKeysByAlert(WebDriverEvent event, String keysToSend) {
        captureScreenShot(event);
	}

    private void captureScreenShot(WebDriverEvent event){
        logEntries.add(event);
		if (TestAdvisorConfiguration.getScreenshotCaptureEnabled()){
			File file = null;
			if (rwd != null)
				file = rwd.getScreenshotAsForTestAdvisor(OutputType.FILE);
			else
				file = tss.getScreenshotAs(OutputType.FILE);
			TestEvent testEvent = createTestEvent(TestEventType.SCREEN_SHOT, event,Level.INFO);
			testEvent.setScreenshotPath(file.getAbsolutePath());
			administrator.getTestCaseExecution().appendEvent(testEvent);
		}
    }
    
}
