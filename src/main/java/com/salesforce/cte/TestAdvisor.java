package com.salesforce.cte;

import com.salesforce.cte.admin.TestAdvisorAdministrator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v96.network.Network;
import org.openqa.selenium.devtools.v96.network.model.Headers;
import org.openqa.selenium.remote.Augmenter;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public class TestAdvisor{

    protected static TestAdvisorAdministrator administrator = TestAdvisorAdministrator.getInstance();

    public static void initializeTestAdvisor(WebDriver driver){
        try {
            driver = new Augmenter().augment(driver);
            DevTools devTools = ((HasDevTools) driver).getDevTools();
            devTools.createSession();
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            HashMap<String, Object> headers = new HashMap<>();
            String traceID = generateTraceId();
            headers.put("x-b3-traceid", traceID);
            headers.put("x-b3-spanid", traceID);
            headers.put("x-b3-sampled", "1");
            devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));
            administrator.getTestCaseExecution().setTraceId(traceID);
        } catch (ClassCastException e) {
            administrator.getTestCaseExecution().setTraceId("");
        }
    }

    private static String generateTraceId() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < 16) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 16);
    }
}
