package com.tmw.tracking.service;

import com.tmw.tracking.domain.MonitoringType;
import com.tmw.tracking.domain.TestResult;

import java.util.List;

/**
 * Created by pzhelnov on 11/11/2016.
 */
public interface MonitoringService {
    List<TestResult> startTests(List<MonitoringType> monitoringTypes);

    TestResult startTest(MonitoringType test);

    List<TestResult> getLastMonitoring();

    void startMonitoring();
}
