package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pzhelnov on 11/18/2016.
 */
@Path("/tracking")
@Singleton
public class TrackingController extends BaseController {

    private final TrackingService trackingService;
    private final static Logger logger = LoggerFactory.getLogger(TrackingController.class);

    @Inject
    public TrackingController(final TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/trackContainer")
    public Viewable getTrackContainer() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("environment", environment);
        return new Viewable("/tracking/trackContainer", vars);
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/find")
    public String getTrackingInfo(@QueryParam("container") final String container) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (StringUtils.isBlank(container)) {
            vars.put("errorMessage", "Incorrect container number");
            return Utils.toJson(vars);
        }
        vars.put("message", trackingService.trackContainer(container));
        return Utils.toJson(vars);
    }
}
