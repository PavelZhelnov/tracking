package com.tmw.tracking.web.service.monitoring;

import com.google.inject.Singleton;
import com.tmw.tracking.domain.TestResult;
import com.tmw.tracking.service.MonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * User: enedzvetsky@provectus-it.com
 */
@Path("/monitoring")
@Singleton
public class MonitoringResource {
    private final static Logger logger = LoggerFactory.getLogger(MonitoringResource.class);
    private final MonitoringService monitoringLogic;


    @Inject
    public MonitoringResource(final MonitoringService monitoringLogic) {
        this.monitoringLogic = monitoringLogic;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lastMonitoring/{token}")
    public List<TestResult> getLastMonitoring() {
        return monitoringLogic.getLastMonitoring();
    }



}
