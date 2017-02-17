package com.tmw.tracking.service.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.utils.DynamicConfig;
import com.tmw.tracking.web.service.exceptions.NotFoundException;
import com.tmw.tracking.web.service.exceptions.ValidationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by ankultepin on 6/3/2015.
 */
@Singleton
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final DynamicConfig dynamicConfig;

    @Inject
    public UserServiceImpl(final UserDao userDao,
                           final DynamicConfig dynamicConfig) {
        this.userDao = userDao;
        this.dynamicConfig = dynamicConfig;
    }

    @Override
    public CredentialsStatus validateUserCredentials(LoginRequest loginRequest) {

        if (loginRequest == null) {
            throw new ValidationException("Login request cannot be null.");
        }
        if (loginRequest.getUserId() == null || StringUtils.isBlank(loginRequest.getUserId())) {
            throw new ValidationException("User ID must be specified.");
        }
        if (loginRequest.getPassword() == null || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new ValidationException("Password must be specified.");
        }
        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();


        User user = userDao.getUserByEmail(userId.toUpperCase());
        if (user == null) {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return new CredentialsStatus(true, "OK");
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public User getAnyUserByEmail(String email) {
        return userDao.getAnyUserByEmail(email);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public User clearRoles(String email) {
        return userDao.clearRoles(email);
    }

    @Override
    public User create(User user) {
        return userDao.create(user);
    }
}
