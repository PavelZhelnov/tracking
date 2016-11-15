package com.tmw.tracking.job;

import com.tmw.tracking.dao.JobStatusInfoDao;
import com.tmw.tracking.entity.JobStatusInfo;
import com.tmw.tracking.job.domain.JobInfo;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author dmikhalishin@provectus-it.com
 */
@Category(TrackingBaseUnitTest.class)
public class GarbageCollectorJobTest extends TrackingBaseUnitTest {

    private EntityManagerProvider entityManagerProvider;
    private GarbageCollectorJob garbageCollectorJob;
    private JobStatusInfoDao jobStatusInfoDao;
    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.entityManagerProvider = injector.getInstance(EntityManagerProvider.class);
        this.garbageCollectorJob = injector.getInstance(GarbageCollectorJob.class);
        this.jobStatusInfoDao = injector.getInstance(JobStatusInfoDao.class);
    }

    @Test
    public void testDroppingHangedJobAndRunningAgain() throws Exception {

        //emulates the situation when some of the job was unexpectedly interrupted
        entityManagerProvider.create();
        JobStatusInfo jobStatusInfo = jobStatusInfoDao.getByName(GarbageCollectorJob.class.getName());
        assertNotNull(jobStatusInfo);
        if (jobStatusInfo.isRunning()) {
            return;
        }
        /*jobStatusInfo.setRunning(true);
        Date passedDate = DateUtils.addDays(new Date(), -10);
        jobStatusInfo.setStartDate(passedDate);
        jobStatusInfo.setStopDate(null);
        jobStatusInfoDao.update(jobStatusInfo);*/

        //checks whether old job will be rescheduled and executed normally
        garbageCollectorJob.execute(null);

        //check status
        JobInfo jobInfo = garbageCollectorJob.getInfo();
        assertNotNull(jobInfo.getStartDate());
        assertNotNull(jobInfo.getEndDate());
        assertFalse(jobInfo.isRunning());

        entityManagerProvider.close();
    }
}