package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.domain.MonitoringType;
import com.tmw.tracking.domain.TestResult;
import com.tmw.tracking.service.MonitoringService;
import com.tmw.tracking.utils.Utils;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/monitoring")
@Singleton
public class MonitoringController extends BaseController {

    private final String serverVersion;
    private final MonitoringService monitoringLogic;

    @Inject
    public MonitoringController(@Named("server.version") final String serverVersion, final MonitoringService monitoringLogic) {
        this.serverVersion = serverVersion;
        this.monitoringLogic = monitoringLogic;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/tests")
    public Viewable getTests() {
        MonitoringType[] values = MonitoringType.values();
        Map<String, String> testMap = new LinkedHashMap<String, String>();
        for(MonitoringType value : values) {
            testMap.put(value.name(), value.getDesc());
        }
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        vars.put("tests", testMap);
        vars.put("version", serverVersion);
        return new Viewable("/monitoring/monitoring", vars);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/start")
    public String startTest(@QueryParam("testId") String testId) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if(!MonitoringType.isExist(testId)) {
            vars.put("errorMessage", "Test not found: " + testId);
        } else {
            List<TestResult> testResults = monitoringLogic.startTests(Arrays.asList(MonitoringType.valueOf(testId)));
            for (TestResult testResult: testResults) {
                testResult.setTestResult(testResult.getTestResult().replaceAll(System.getProperty("line.separator"),"<br>"));
            }
            vars.put("results", testResults);
        }
        return Utils.toJson(vars);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/lastMonitoring")
    public String getLastMonitoring() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        List<TestResult> testResults = monitoringLogic.getLastMonitoring();
        vars.put("results", testResults);
        return Utils.toJson(vars);
    }




}
