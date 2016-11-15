package com.tmw.tracking.job;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.dao.JobStatusInfoDao;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;

/**
 * Remove orders that were not scheduled more than one week
 * Remove signatures older then 6 month
 * @author dmikhalishin@provectus-it.com
 * @see TrackingJob
 */
@Singleton
public class GarbageCollectorJob extends TrackingJob {

    private static final int DEFAULT_PERIOD_SEVEN_DAYS = 7;

    @Inject
    public GarbageCollectorJob(final EntityManagerProvider entityManagerProvider,
                               final JobStatusInfoDao infoDao) {
        super(entityManagerProvider, infoDao);
    }

    /**
     * {@inheritDoc}
     * @see TrackingJob#getName()
     */
    @Override
    protected String getName() {
        return GarbageCollectorJob.class.getName();
    }

    /**
     * {@inheritDoc}
     * @see TrackingJob#getDescription()
     */
    @Override
    protected String getDescription() {
        return "Job that removes active/saved orders, obsolete store device records, old customer signatures and moves 'While You Wait' orders to picked up status; ";
    }

    /**
     * {@inheritDoc}
     * @see TrackingJob#executeLogic()
     */
    @Override
    protected void executeLogic() throws Exception {
    }
}
