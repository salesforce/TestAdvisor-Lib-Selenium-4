/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.cte.test.webdriver;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.testng.Assert;

import static org.openqa.selenium.remote.DriverCommand.*;
import static org.testng.Assert.assertNotNull;


/**
 * @author gneumann
 *
 */
public class MockCommandExecutor implements CommandExecutor {
	public static final String SCRIPT_EXECUTED = "script executed";
	public static final String ELEMENT_ID_PREFIX = "Elem";
	public static final String CHILD_ELEMENT_ID_PREFIX = "ChildElem";
	public static final String STATE_OK = "OK";
	public static final String STRING_ALLISWELL_VALUE = "All is well";
	public static final String STATE_EXCEPTION = "Exception";
	
	private static boolean doTriggerWebDriverException;
	private static boolean doUseSpecificReturnValue;
	private static String stringReturnValue;

	private RemoteWebDriver webDriver;
	private String id;

	public void setRemoteWebDriver(RemoteWebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public Response execute(Command command) throws IOException {
		if (doTriggerWebDriverException) {
			// automatically reset flag so that exception
			// thrown below is a one-time-thing
			doTriggerWebDriverException = false;
			throw new WebDriverException(STATE_EXCEPTION);
		}

		Response response = new Response();
    	response.setState(STATE_OK);
   	
	    if (FIND_ELEMENT.equals(command.getName())) {
			id = ELEMENT_ID_PREFIX + System.currentTimeMillis();
			RemoteWebElement rwe = new RemoteWebElement();
			rwe.setId(id);
			rwe.setParent(webDriver);
			response.setValue(rwe);
		} else if (FIND_ELEMENTS.equals(command.getName())) {
			id = ELEMENT_ID_PREFIX + System.currentTimeMillis();
			RemoteWebElement rwe = new RemoteWebElement();
			rwe.setId(id);
			rwe.setParent(webDriver);
			List<WebElement> elems = new ArrayList<WebElement>();
			elems.add(rwe);
			response.setValue(elems);
	    } else if (CLICK_ELEMENT.equals(command.getName())) {
	    	// zero argument command
			response.setValue(STATE_OK);
	    } else if (FIND_CHILD_ELEMENT.equals(command.getName())) {
	    	// one argument command
	    	id = CHILD_ELEMENT_ID_PREFIX + System.currentTimeMillis();
	    	RemoteWebElement rwe = new RemoteWebElement();
	    	rwe.setId(id);
	    	rwe.setParent(webDriver);
	    	response.setValue(rwe);
	    } else if (GET_TITLE.equals(command.getName())) {
	    	// zero argument command
			response.setValue(STATE_OK);
	    } else if (GET.equals(command.getName())) {
	    	// one argument command
	    	String url = getStringValueFromParameters(command, "url");
	    	response.setValue(url);
	    } else if (EXECUTE_SCRIPT.equals(command.getName())) {
	    	String script = getStringValueFromParameters(command, "script");
	    	assertNotNull(script);
	    	if (script.contains("style.border='3px solid")) {
	    		response.setValue("highlighted web element");
	    	} else {
	    		response.setValue(SCRIPT_EXECUTED);
	    	}
	    } else if (EXECUTE_ASYNC_SCRIPT.equals(command.getName())){
			response.setValue(SCRIPT_EXECUTED);
		} else if (NEW_SESSION.equals(command.getName())) {
	    	Map<String, Object> rawCapabilities = new HashMap<>();
	    	response.setValue(rawCapabilities);
	    	response.setSessionId(MockRemoteWebDriver.DUMMY_SESSION_ID);
	    } else if(SCREENSHOT.equals(command.getName()) || ELEMENT_SCREENSHOT.equals(command.getName())){
			response.setValue(Base64.getEncoder().encodeToString(STRING_ALLISWELL_VALUE.getBytes()));
		} else if(SUBMIT_ELEMENT.equals(command.getName())) {
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (SEND_KEYS_TO_ELEMENT.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (CLEAR_ELEMENT.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_ELEMENT_TAG_NAME.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_ELEMENT_DOM_PROPERTY.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_ELEMENT_DOM_ATTRIBUTE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_ELEMENT_ARIA_ROLE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_ELEMENT_ACCESSIBLE_NAME.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (IS_ELEMENT_SELECTED.equals(command.getName())){
			if (doUseSpecificReturnValue) {
				// reset flag
				doUseSpecificReturnValue = false;
				// use return value of wrong type
				response.setValue(stringReturnValue);
			} else {
				response.setValue(Boolean.valueOf(true));
			}
		} else if (IS_ELEMENT_ENABLED.equals(command.getName())){
			if (doUseSpecificReturnValue) {
				// reset flag
				doUseSpecificReturnValue = false;
				// use return value of wrong type
				response.setValue(stringReturnValue);
			} else {
				response.setValue(Boolean.valueOf(true));
			}
		} else if (IS_ELEMENT_DISPLAYED.equals(command.getName())){
			if (doUseSpecificReturnValue) {
				// reset flag
				doUseSpecificReturnValue = false;
				// use return value of wrong type
				response.setValue(stringReturnValue);
			} else {
				response.setValue(Boolean.valueOf(true));
			}
		} else if (GET_ELEMENT_LOCATION.equals(command.getName())){
			Map<String, Object> rawPoint = new HashMap<>();
			rawPoint.put("x", 0);
			rawPoint.put("y", 0);
			response.setValue(rawPoint);
		} else if (GET_ELEMENT_SIZE.equals(command.getName())){
			Map<String, Object> rawSize = new HashMap<>();
			rawSize.put("width", 0);
			rawSize.put("height", 0);
			response.setValue(rawSize);
		} else if (GET_ELEMENT_RECT.equals(command.getName())){
			Map<String, Object> rawRect = new HashMap<>();
			rawRect.put("x", (Number) 0);
			rawRect.put("y", (Number) 0);
			rawRect.put("width", (Number) 0);
			rawRect.put("height", (Number) 0);
			response.setValue(rawRect);
		} else if (GET_ELEMENT_LOCATION_ONCE_SCROLLED_INTO_VIEW.equals(command.getName())){
			Map<String, Object> rawPoint = new HashMap<>();
			rawPoint.put("x", 0);
			rawPoint.put("y", 0);
			response.setValue(rawPoint);
		} else if (GET_ELEMENT_SHADOW_ROOT.equals(command.getName())){
			response.setValue(webDriver);
		} else if (GET_ELEMENT_VALUE_OF_CSS_PROPERTY.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (DISMISS_ALERT.equals(command.getName())){
			response.setValue(STATE_OK);
		} else if (ACCEPT_ALERT.equals(command.getName())){
			response.setValue(STATE_OK);
		} else if (SEND_KEYS_TO_ELEMENT.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GO_BACK.equals(command.getName())){
			response.setValue(STATE_OK);
		} else if (GO_FORWARD.equals(command.getName())){
			response.setValue(STATE_OK);
		} else if (GET.equals(command.getName())){
			response.setValue(STATE_OK);
		} else if (SET_CURRENT_WINDOW_SIZE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (SET_CURRENT_WINDOW_POSITION.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (REFRESH.equals(command.getName())){
			response.setValue(STATE_OK);
		} else if (GET_ACTIVE_ELEMENT.equals(command.getName())){
			id = ELEMENT_ID_PREFIX + System.currentTimeMillis();
			RemoteWebElement rwe = new RemoteWebElement();
			rwe.setId(id);
			rwe.setParent(webDriver);
			response.setValue(rwe);
		} else if (SWITCH_TO_FRAME.equals(command.getName())){
			response.setValue(STATE_OK);
		} else if (SWITCH_TO_NEW_WINDOW.equals(command.getName())){
			Map<String, Object> handles = new HashMap<>();
			handles.put("handle", STRING_ALLISWELL_VALUE);
			response.setValue(handles);
		} else if (GET_CURRENT_URL.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_PAGE_SOURCE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (CLOSE.equals(command.getName())){
			// zero argument command
			response.setValue(STATE_OK);
		} else if (QUIT.equals(command.getName())){
			// zero argument command
			response.setValue(STATE_OK);
		} else if (GET_CURRENT_WINDOW_HANDLE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (DELETE_ALL_COOKIES.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (ADD_COOKIE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_ALL_COOKIES.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_COOKIE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (DELETE_COOKIE.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (SET_TIMEOUT.equals(command.getName())){
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (GET_ALERT_TEXT.equals(command.getName())) {
			response.setValue(STRING_ALLISWELL_VALUE);
		} else if (DISMISS_ALERT.equals(command.getName())) {
			response.setValue(STATE_OK);
		} else {
	    	System.out.println(String.format("Command %s not yet covered by %s", command.getName(), this.getClass().getName()));
	    }
		return response;
	}
	
	public static void setDoTriggerWebDriverException() {
		doTriggerWebDriverException = true;
	}

	public static void setReturnValue(String returnValue) {
		doUseSpecificReturnValue = true;
		stringReturnValue = returnValue;
	}

	private String getStringValueFromParameters(Command command, String key) {
		String value = null;
		if (command.getParameters() != null) {
			value = (String) command.getParameters().get(key);			
		} else {
			Assert.fail("Command parameters not set");
		}
		return value;
	}
}
