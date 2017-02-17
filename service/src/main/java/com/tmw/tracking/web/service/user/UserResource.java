package com.tmw.tracking.web.service.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.web.service.exceptions.NotFoundException;
import com.tmw.tracking.web.service.exceptions.ValidationException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
@Singleton
public class UserResource {

    private final UserDao userDao;
    private final UserService userService;

    @Inject
    public UserResource(
            final UserDao userDao,
            final UserService userService) {
        this.userDao = userDao;
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllUsers")
    public List<User> getUsers() {
        return userDao.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get")
    public User getUser(@QueryParam("id") final Long id) {
        if (id == null)
            throw new ValidationException("ID cannot be null.");
        final User user = userDao.getById(id);
        if (user == null)
            throw new NotFoundException("User was not found.");
        return user;
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/validateUserCredentials/{token}")
    public UserService.CredentialsStatus validateUserCredentials(final LoginRequest loginRequest) {
        return userService.validateUserCredentials(loginRequest);
    }


}
