package com.tmw.tracking.web.service.user;

import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertTrue;

/**
 * @author dmikhalishin@provectus-it.com
 * @see TrackingBaseUnitTest
 */
@Category(TrackingBaseUnitTest.class)
public class UserServiceTest extends TrackingBaseUnitTest {

    private UserResource userResource;

    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        userResource = injector.getInstance(UserResource.class);
    }


    @Test(expected = ServiceException.class)
    public void testIncorrectCredentialsNullCredentials() {
        userResource.validateUserCredentials(null);
    }

    @Test(expected = ServiceException.class)
    public void testIncorrectCredentialsEmptyUserId() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("bla");
        loginRequest.setUserId("");
        userResource.validateUserCredentials(null);
    }

    @Test
    public void testIncorrectCredentialsEmptyPassword() {
        try {
            userResource.validateUserCredentials(null);
        }
        catch (Exception e) {
            assertTrue(e.getMessage().contains("Login request cannot be null"));
        }
    }
}
