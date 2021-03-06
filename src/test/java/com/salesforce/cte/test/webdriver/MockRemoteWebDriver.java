/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.cte.test.webdriver;

import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;

import com.google.common.collect.ImmutableMap;

/**
 * @author gneumann
 *
 */
public class MockRemoteWebDriver extends RemoteWebDriver implements  HasDevTools {
	public static final String DUMMY_SESSION_ID = "dummy-session-id";
	
	public MockRemoteWebDriver(MockCommandExecutor mockCommandExecutor, Capabilities caps) {
		super(mockCommandExecutor, caps);
		setCommandExecutor(mockCommandExecutor);
		setSessionId(DUMMY_SESSION_ID);
	}
	
	protected Response execute(String command, Map<String, ?> parameters) {
		return super.execute(command, parameters);
	}

	protected Response execute(String command) {
		return execute(command, ImmutableMap.of());
	}

	@Override
	public Optional<DevTools> maybeGetDevTools() {
		return Optional.of(mock(DevTools.class));
	}
}
