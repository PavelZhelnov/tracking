package com.tmw.tracking.web.service.auth;

import com.google.inject.Singleton;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.domain.LoginResponse;
import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.utils.DynamicConfig;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Authentication service
 *
 * @author dmikhalishin@provectus-it.com
 */
@Path("/auth")
@Singleton
public class AuthenticationResource {

    private final AuthenticationService authenticationLogic;
    private final DynamicConfig dynamicConfig;
    private final String clientVersion;

    @Inject
    public AuthenticationResource(final AuthenticationService authenticationLogic, DynamicConfig dynamicConfig, @Named("client.version") String clientVersion) {
        this.authenticationLogic = authenticationLogic;
        this.dynamicConfig = dynamicConfig;
        this.clientVersion = clientVersion;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public LoginResponse login(final LoginRequest loginRequest) {
        validateLoginRequest(loginRequest);
        final AuthenticatedUser authenticatedUser = authenticationLogic.login(loginRequest.getUserId().toUpperCase(), loginRequest.getPassword());
        return new LoginResponse(authenticatedUser.getToken(), authenticatedUser.getExpired());
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Object logout(@QueryParam("token") final String token) {
        if (token == null)
            throw new ServiceException("Token cannot be null.");
        authenticationLogic.logout(token);
        return null/* return nothing */;
    }


    // ------------------------------------------------------------------------

    /**
     * Validate {@link LoginRequest}
     *
     * @param loginRequest the {@code LoginRequest}. Can be {@code null}
     */
    private void validateLoginRequest(final LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new ServiceException("Login request cannot be null.");
        }
        if (StringUtils.isBlank(loginRequest.getUserId())) {
            throw new ServiceException("User ID must be specified.");
        }
    }
}
