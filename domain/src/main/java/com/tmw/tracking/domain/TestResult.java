package com.tmw.tracking.domain;

/**
 * User: enedzvetsky@provectus-it.com
 */
public class TestResult {
    public static final String ALL_WORKING_NORMALLY="All working normally";

    private MonitoringType monitoringType;
    private String testResult;
    private boolean success;
    private long startTime;
    private long endTime;

    public TestResult() {
    }

    public TestResult(final MonitoringType monitoringType) {
        startTime = System.currentTimeMillis();
        this.monitoringType = monitoringType;
    }

    public MonitoringType getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(MonitoringType monitoringType) {
        this.monitoringType = monitoringType;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
        endTime = System.currentTimeMillis();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDesc(){
        return monitoringType.getDesc();
    }

    public long getResponseTime(){
        return endTime - startTime;
    }
}
