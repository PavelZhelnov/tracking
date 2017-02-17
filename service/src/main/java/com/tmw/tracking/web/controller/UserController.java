package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Path("/userstore")
@Singleton
public class UserController extends BaseController {

    private final UserService userService;
    private final RoleDao roleDao;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    public UserController(final UserService userService, final RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/userManagement")
    public Viewable getUserManagement() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("roles", roleDao.getAll());
        vars.put("environment", environment);
        return new Viewable("/userstore/userManagement", vars);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/find")
    public String getUser(@QueryParam("email") final String email) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (StringUtils.isBlank(email)) {
            vars.put("errorMessage", "Incorrect email");
            return Utils.toJson(vars);
        }
        // fix me should be visibility
        User authUser = AuthenticationServiceImpl.getAuthenticatedUser();
        User user = userService.getAnyUserByEmail(email.toUpperCase());
        if (user == null) {
            vars.put("errorMessage", "User not found");
            return Utils.toJson(vars);
        }
        if (user.equals(authUser)) {
            vars.put("errorMessage", "User can't edit himself");
            return Utils.toJson(vars);
        }
        vars.put("user", user);
        return Utils.toJson(vars);
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/switch")
    public String deleteUser(@QueryParam("email") final String email, @QueryParam("mode") final Boolean mode) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        User user = userService.getAnyUserByEmail(email);
        if (user == null) {
            vars.put("errorMessage", "User was not found: " + email);
        } else {
            user.setActive(mode);
            userService.update(user);
        }
        return Utils.toJson(vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/clearRoles")
    public String clearUserRoles(@QueryParam("email") final String email) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        User user = userService.clearRoles(email);
        if (user == null) {
            vars.put("errorMessage", "User was not found: " + email);
        }
        vars.put("user", user);
        return Utils.toJson(vars);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/save")
    public String saveUser(User userFromClient) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        String email = userFromClient.getEmail();
        if (StringUtils.isBlank(email)) {
            vars.put("errorMessage", "Email is empty");
            return Utils.toJson(vars);
        }
        User user = userService.getAnyUserByEmail(email);
        String firstName = userFromClient.getFirstName();
        if (StringUtils.isBlank(firstName)) {
            vars.put("errorMessage", "First name is empty");
            return Utils.toJson(vars);
        }
        String lastName = userFromClient.getLastName();
        if (StringUtils.isBlank(lastName)) {
            vars.put("errorMessage", "Last name is empty");
            return Utils.toJson(vars);
        }
        Collection<Role> roles = userFromClient.getRoles();
        if (user == null) {
            user = new User();
            user.setEmail(email.toUpperCase());
            user.setActive(true);
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLastUpdated(new Date());
        user.setPassword(Utils.encryptPassword(userFromClient.getPassword()));

        Set<Role> roleHashSet = new HashSet<Role>();
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                Role roleFromDb = roleDao.getByRoleType(role.getType());
                roleHashSet.add(roleFromDb);
            }
        }
        user.setRoles(roleHashSet);
        try {
            if (user.getId() == null) {
                userService.create(user);
            } else {
                userService.update(user);
            }
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during saving user " + e.getMessage());
        }
        return Utils.toJson(vars);
    }




}
