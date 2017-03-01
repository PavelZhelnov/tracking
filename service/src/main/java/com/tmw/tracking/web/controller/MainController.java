package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.job.domain.JobInfo;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
@Singleton
public class MainController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);

    private final String serverVersion;

    @Inject
    public MainController(@Named("server.version") final String serverVersion,
                          @Named("major.version") final String majorVersion) {
        this.serverVersion = majorVersion+"_"+serverVersion;
    }


    // ------------------------------------------------------------------------

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/status")
    public Viewable getStatusInfo() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("version", serverVersion);
        vars.put("environment", environment);
        final List<JobInfo> jobInfo = new ArrayList<JobInfo>();
        vars.put("jobInfo", jobInfo);
        vars.put("angular", true);
        return new Viewable("/status", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    public Viewable loginGet() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        return new Viewable("/login", vars);
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    public Viewable loginPost(@Context final javax.servlet.http.HttpServletRequest req) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        vars.put("error", req.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME) != null);
        vars.put("username", req.getParameter("username"));
        vars.put("password", req.getParameter("password"));
        return new Viewable("/login", vars);
    }

}
