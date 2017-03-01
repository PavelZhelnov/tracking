package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.support.RoleInfo;
import com.tmw.tracking.service.PermissionService;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/userstore")
@Singleton
public class UserController extends BaseController {

    private final UserService userService;
    private final RoleDao roleDao;
    private final PermissionService permissionService;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    public UserController(final UserService userService, final RoleDao roleDao, final PermissionService permissionService) {
        this.userService = userService;
        this.roleDao = roleDao;
        this.permissionService = permissionService;
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
    @Produces(MediaType.TEXT_HTML)
    @Path("/roleManagement")
    public Viewable getRoleManagement() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("roles", roleDao.getAll());
        vars.put("permissions", permissionService.getAllPermissions());
        vars.put("environment", environment);
        return new Viewable("/userstore/roleManagement", vars);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllRoles")
    public List<Role> getAllRoles() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        return roleDao.getAll();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllPermissions")
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getRole")
    public RoleInfo getRole(@QueryParam("id") final Long id) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRole(roleDao.getById(id));
        roleInfo.setPermissions(roleInfo.getRole().getPermissionList());
        roleInfo.setAllPermissions(permissionService.getAllPermissions());
        return roleInfo;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/saveRole")
    public String saveRole(RoleInfo roleInfo) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (roleInfo.getRole() == null) {
            vars.put("errorMessage", "Role is empty");
            return Utils.toJson(vars);
        }

        Role role = roleInfo.getRole().getId() != null?roleDao.getById(roleInfo.getRole().getId()): new Role();
        role.setRoleName(roleInfo.getRole().getRoleName());
        role.setPermissionList(roleInfo.getPermissions());
        try {
            roleDao.update(role);
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during saving Role " + e.getMessage());
        }
        return Utils.toJson(vars);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteRole")
    public String deleteRole(RoleInfo roleInfo) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (roleInfo.getRole() == null) {
            vars.put("errorMessage", "role is empty");
            return Utils.toJson(vars);
        }

        Role role = roleDao.getById(roleInfo.getRole().getId());
        if (role == null) {
            vars.put("errorMessage", "role is not found");
            return Utils.toJson(vars);
        }
        try {
            roleDao.delete(role);
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during deleting Role " + e.getMessage());
        }
        return Utils.toJson(vars);
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
                Role roleFromDb = roleDao.getByRoleName(role.getRoleName());
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
