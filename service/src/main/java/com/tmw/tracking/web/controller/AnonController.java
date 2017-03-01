package com.tmw.tracking.web.controller;

import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pzhelnov on 2/20/2017.
 */
@Path("/anon")
@Singleton

public class AnonController extends BaseController {

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/index")
    public Viewable getIndexPage() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("environment", environment);
        return new Viewable("/anon/index", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/showpage")
    public Viewable getContact(@QueryParam("page") String page) throws  ServerException {
        if ("test".equals(page)) {
            throw new ServerException("Test exception");
        }
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("environment", environment);
        return new Viewable("/anon/" + page, vars);
    }

}
