package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.dao.ContainerTypeDao;
import com.tmw.tracking.dao.impl.DriverDao;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.entity.Driver;
import com.tmw.tracking.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/dict")
@Singleton
public class DictController extends BaseController {

    private final ContainerTypeDao containerTypeDao;
    private final DriverDao driverDao;
    private final static Logger logger = LoggerFactory.getLogger(DictController.class);

    @Inject
    public DictController(
            final ContainerTypeDao containerTypeDao,
            final DriverDao driverDao) {
        this.containerTypeDao = containerTypeDao;
        this.driverDao = driverDao;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/containerTypes")
    public Viewable getContainerTypes() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("containers", containerTypeDao.getAll());
        vars.put("environment", environment);
        return new Viewable("/dict/containerTypes", vars);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllContainerTypes")
    public List<ContainerType> getAllRoles() {
        return containerTypeDao.getAll();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getContainerType")
    public ContainerType getRole(@QueryParam("id") final Long id) {
        return containerTypeDao.getById(id);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/saveContainerType")
    public String saveRole(ContainerType containerType) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        try {
            containerTypeDao.update(containerType);
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during saving Container Type" + e.getMessage());
        }
        return Utils.toJson(vars);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteContainerType")
    public String deleteRole(ContainerType containerType) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (containerType == null) {
            vars.put("errorMessage", "Container Type is empty");
            return Utils.toJson(vars);
        }

        ContainerType ct = containerTypeDao.getById(containerType.getId());
        if (ct == null) {
            vars.put("errorMessage", "Container Type is not found by id");
            return Utils.toJson(vars);
        }
        try {
            containerTypeDao.delete(containerType);
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during deleting Container Type" + e.getMessage());
        }
        return Utils.toJson(vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/drivers")
    public Viewable getDriversPage() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("drivers", driverDao.getAll());
        vars.put("environment", environment);
        return new Viewable("/dict/drivers", vars);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllDrivers")
    public List<Driver> getAllDrivers() {
        return driverDao.getAll();
    }

}
