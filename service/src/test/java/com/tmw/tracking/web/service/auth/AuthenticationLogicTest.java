package com.tmw.tracking.web.service.auth;

import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertNotNull;

@Category(TrackingBaseUnitTest.class)
public class AuthenticationLogicTest extends TrackingBaseUnitTest {

    public static final String SHORT_NAME = "PHD2";
    public static final Long USER_ID = 765L;

    private AuthenticationService logic;

    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        logic = injector.getInstance(AuthenticationServiceImpl.class);
    }

    @Test
    public void testLogin(){
        final AuthenticatedUser user = logic.login(SHORT_NAME,null);
        assertNotNull(user);
        assertNotNull(user.getToken());
        assertNotNull(user.getUser());
        assertNotNull(user.getExpired());
        Assert.assertEquals(user.getUser().getId(),USER_ID);
    }

    @Test
    public void testValidateUserWithCorrectToken(){
        final AuthenticatedUser user = logic.login(SHORT_NAME,null);
        assertNotNull(user);
        final User loggedUser = logic.validateUser(user.getToken());
        assertNotNull(loggedUser);
        Assert.assertEquals(loggedUser.getId(), user.getUser().getId());
    }


    @Test(expected = ServiceException.class)
    public void testUserLogout(){
        final AuthenticatedUser user = logic.login(SHORT_NAME,null);
        assertNotNull(user);
        logic.logout(user.getUser());
        logic.validateUser(user.getToken());
    }


}
