package com.tmw.tracking.web.service.user;

import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author dmikhalishin@provectus-it.com
 * @see TrackingBaseUnitTest
 */
@Category(TrackingBaseUnitTest.class)
public class TrackingServiceTest extends TrackingBaseUnitTest {

    private TrackingService trackingService;

    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        trackingService = injector.getInstance(TrackingService.class);
    }


    @Test
    public void testGetMessage() {
        assertNotNull(trackingService.trackContainer("ZIM394928734"));
    }

}
