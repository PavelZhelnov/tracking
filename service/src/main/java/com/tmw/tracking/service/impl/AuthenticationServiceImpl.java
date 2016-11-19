package com.tmw.tracking.service.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.AuthenticatedUserDao;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.utils.DynamicConfig;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.MDC;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.UUID;

/**
 * Authentication logic
 *
 * @author dmikhalishin@provectus-it.com
 */
@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticatedUserDao authenticatedUserDao;
    private final RoleDao roleDao;
    private final UserDao userDao;
    private final DynamicConfig dynamicConfig;
    private final Integer tokenExpirationMinutes;

    @Inject
    public AuthenticationServiceImpl(final AuthenticatedUserDao authenticatedUserDao,
                                     final RoleDao roleDao,
                                     final UserDao userDao,
                                     final DynamicConfig dynamicConfig,
                                     @Named("tmw.auth.token.expiration")final Integer tokenExpirationMinutes
                                     ) {
        this.authenticatedUserDao = authenticatedUserDao;
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.dynamicConfig = dynamicConfig;
        this.tokenExpirationMinutes = tokenExpirationMinutes;
    }

    @Override
    public AuthenticatedUser login(final String email, final String password) {
        User user;
        MDC.put(Utils.MDC_USER, email);
        user = userDao.getAnyUserByEmail(email);
        if (user == null) {
            throw new ServiceException("User ["+email+"] not recognized. Please provide password.", false);
        }
        if(!user.isActive()){
            throw new ServiceException("User ["+email+"] is not active, login denied", false);
        }
        if (!user.getPassword().equals(Utils.encryptPassword(password))) {
            throw new ServiceException("The credentials are incorrect!", false);
        }
//        } else {
//            user = createOrUpdateUser(email, password);
//        }
        return loginUser(user);
    }


    private AuthenticatedUser loginUser(User user) {
        AuthenticatedUser authenticatedUser = authenticatedUserDao.getAuthenticatedUser(user);
        if (authenticatedUser == null) {
            authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setUser(user);
        }
        setAsAuthenticated(authenticatedUser);

        return authenticatedUser;
    }


    @Override
    public User createOrUpdateUser(final String email, final String password) {
        User user = userDao.getAnyUserByEmail(email);
        if (user == null) {
            user = createUser(email, password);
        } else {
            updateUser(user);
        }
        return user;
    }

    /**
     * Validate user
     *
     * @param token the authentication token. Can be {@code null}
     * @return the authenticated {@code User}
     */
    @Override
    public User validateUser(final String token) {
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("Token cannot be blank");
        }
        final AuthenticatedUser authenticatedUser = authenticatedUserDao.getAuthenticatedUserByToken(token);
        if (authenticatedUser == null) {
            throw new ServiceException("Token is invalid. User is not logged.");
        }
        if (authenticatedUser.getExpired().before(new Date())) {
            throw new ServiceException("Token is expired.");
        }
        return authenticatedUser.getUser();
    }

    /**
     * Logout functionality
     *
     * @param token he authentication token. Can be {@code null}
     */
    @Override
    @Transaction
    public void logout(final String token) {
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("Token cannot be blank");
        }

        final AuthenticatedUser authenticatedUser = authenticatedUserDao.getAuthenticatedUserByToken(token);
        if (authenticatedUser != null) {
            authenticatedUserDao.delete(authenticatedUser);
        }
    }

    /**
     * Logout functionality
     *
     * @param user the authenticated user. Cannot be {@code null}
     */
    @Override
    public void logout(final User user) {
        if (user == null) {
            return;
        }
        final AuthenticatedUser authenticatedUser = authenticatedUserDao.getAuthenticatedUser(user);
        if (authenticatedUser != null) {
            authenticatedUserDao.delete(authenticatedUser);
        }
    }


    // ------------------------------------------------------------------------

    /**
     * Save/Update {@link AuthenticatedUser authenticated user}
     *
     * @param authenticatedUser the {@code AuthenticatedUser}. Cannot be {@code null}
     */
    private void setAsAuthenticated(final AuthenticatedUser authenticatedUser) {
        final String token = UUID.randomUUID().toString();
        authenticatedUser.setToken(token);
        final Date expired = DateUtils.addMinutes(new Date(), tokenExpirationMinutes);
        authenticatedUser.setExpired(expired);
        if (authenticatedUser.getId() == null) {
            authenticatedUserDao.create(authenticatedUser);
        }
        else {
            authenticatedUserDao.update(authenticatedUser);
        }
    }

    private User createUser(final String email, final String password) {
        final User user = new User();
        user.setActive(true);
        user.setEmail(email);
        user.setPassword(Utils.encryptPassword(password));
        user.setFirstName(email);
        user.setLastName(email);
        userDao.create(user);
        return user;
    }


    private void updateUser(final User user) {
        userDao.update(user);
    }



    public static User getAuthenticatedUser() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser;
        }
        throw new ServiceException("Token is invalid. User is not logged.");
    }

    public static User getCurrentUser() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() && subject.getPrincipal() != null) {
            return (User) subject.getPrincipal();
        } else {
            return null;
        }
    }

}
