package com.tmw.tracking.web.service.tracking;

import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static junit.framework.Assert.assertNotNull;

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
