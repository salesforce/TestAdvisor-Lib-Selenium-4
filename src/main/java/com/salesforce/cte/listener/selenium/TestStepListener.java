/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.cte.listener.selenium;

import java.util.List;

import com.salesforce.cte.common.TestEvent;
import com.salesforce.cte.common.TestEventType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.console.model.ConsoleMessage.Level;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestStepListener extends AbstractEventListener {
    protected RemoteWebDriver rwd;

	@Override
	public void setWebDriver(WebDriver driver) {
		this.rwd = (RemoteWebDriver) driver;
	}
    
    /*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/
    @Override
	public void beforeClose(WebDriverEvent event) {
        captureTestStep(event); 
	}

	@Override
	public void beforeGet(WebDriverEvent event, String url) {
		captureTestStep(event);
	}

    /*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

    @Override
	public void beforeExecuteScript(WebDriverEvent event, String script, List<Object> convertedArgs) {
        if (script.contains("click")){
            captureTestStep(event);
        }
	}

    /*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/
    @Override
	public void beforeBack(WebDriverEvent event) {
        captureTestStep(event);
	}

	@Override
	public void beforeForward(WebDriverEvent event) {
        captureTestStep(event);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeClick(WebDriverEvent event, WebElement element) {
        captureTestStep(event);   
	}

	@Override
	public void beforeClear(WebDriverEvent event, WebElement element) {
        captureTestStep(event);   
	}

	@Override
	public void beforeSendKeysByElement(WebDriverEvent event, WebElement element, CharSequence... keysToSend) {
		// Skip capturing a url if it is the same locator, because it means
		// a test is sending text character by character to the same text field.
		if (isDifferentLocator(element)){
            captureTestStep(event);
        }
	}

	@Override
	public void beforeSubmit(WebDriverEvent event, WebElement element) {
        captureTestStep(event);   
    }

    /*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Alert object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeDismiss(WebDriverEvent event) {
        captureTestStep(event);
	}

	@Override
	public void beforeAccept(WebDriverEvent event) {
        captureTestStep(event);
	}

	@Override
	public void beforeSendKeysByAlert(WebDriverEvent event, String keysToSend) {
        captureTestStep(event);
	}

    private void captureTestStep(WebDriverEvent event){
        logEntries.add(event);
        TestEvent testEvent = new TestEvent(TestEventType.URL, rwd.getCurrentUrl(), Level.INFO.toString().toUpperCase());
        testEvent.setSeleniumCmd(event.getCmd().getLongCmdString());
        testEvent.setSeleniumLocator(event.getElementLocator());
        administrator.getTestCaseExecution().appendEvent(testEvent);
    }
}
