package com.tmw.tracking.dao;

import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import junit.framework.Assert;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.persistence.EntityManager;
import java.util.Date;

@Category(TrackingBaseUnitTest.class)
public class AuthenticatedUserDaoTest extends TrackingBaseUnitTest {

    private static final String USER = "PZ7";
    private static final String PASSWORD = "TEST123";

    private AuthenticatedUserDao authenticatedUserDao;
    private AuthenticationService authenticationLogic;
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        entityManager = injector.getInstance(EntityManager.class);
        authenticatedUserDao = injector.getInstance(AuthenticatedUserDao.class);
        authenticationLogic = injector.getInstance(AuthenticationServiceImpl.class);
    }

    @Test
    public void testGetAuthenticatedUser(){
        final AuthenticatedUser authenticatedUser = authenticationLogic.login(USER,PASSWORD);
        final AuthenticatedUser user = authenticatedUserDao.getAuthenticatedUser(authenticatedUser.getUser());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getToken(), authenticatedUser.getToken());
        Assert.assertEquals(user.getUser().getId(), authenticatedUser.getUser().getId());
        Assert.assertEquals(user.getUser().getEmail(), authenticatedUser.getUser().getEmail());
    }

    @Test
    public void testGetAuthenticatedUserByToken(){
        final AuthenticatedUser authenticatedUser = authenticationLogic.login(USER,PASSWORD);
        final AuthenticatedUser user = authenticatedUserDao.getAuthenticatedUserByToken(authenticatedUser.getToken());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getToken(), authenticatedUser.getToken());
        Assert.assertEquals(user.getUser().getId(), authenticatedUser.getUser().getId());
        Assert.assertEquals(user.getUser().getEmail(), authenticatedUser.getUser().getEmail());
    }

    @Test
    public void testCreateDelete(){
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        String token = "test"+System.currentTimeMillis();
        authenticatedUser.setToken(token);
        authenticatedUser.setExpired(DateUtils.addMinutes(new Date(), 1));
        authenticatedUser.setUser(entityManager.find(User.class, 214L));
        authenticatedUserDao.create(authenticatedUser);
        AuthenticatedUser user = authenticatedUserDao.getAuthenticatedUser(authenticatedUser.getUser());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getToken(), authenticatedUser.getToken());
        Assert.assertEquals(user.getUser().getId(), authenticatedUser.getUser().getId());
        Assert.assertEquals(user.getUser().getEmail(), authenticatedUser.getUser().getEmail());

        user.setToken("updated"+token);
        authenticatedUserDao.update(user);
        user = authenticatedUserDao.getAuthenticatedUserByToken(authenticatedUser.getToken());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getToken(), "updated"+token);
        Assert.assertEquals(user.getUser().getId(), authenticatedUser.getUser().getId());
        Assert.assertEquals(user.getUser().getEmail(), authenticatedUser.getUser().getEmail());
        // delete
        authenticatedUserDao.delete(user);
        Assert.assertNull(authenticatedUserDao.getAuthenticatedUser(user.getUser()));

    }

}
