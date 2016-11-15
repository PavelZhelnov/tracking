package com.tmw.tracking.job;

import com.tmw.tracking.dao.JobStatusInfoDao;
import com.tmw.tracking.entity.JobStatusInfo;
import com.tmw.tracking.job.domain.JobInfo;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import org.apache.log4j.MDC;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

public abstract class TrackingJob implements InterruptableJob {

    public static final long JOB_TIMEOUT = 120;   //minutes
    public static final int SHUTDOWN_TIMEOUT = 60;   //seconds

    //now the longest run time approximately 40 min
    public static final int MAX_DURATION_EXECUTION_JOB_IN_HOURS = 1;

    public static final int N_THREADS = 10;
    protected final static Logger logger = LoggerFactory.getLogger(TrackingJob.class);

    protected final EntityManagerProvider entityManagerProvider;
    protected final JobStatusInfoDao infoDao;

    protected volatile boolean isInterrupted = false;

    TrackingJob(final EntityManagerProvider entityManagerProvider,
                final JobStatusInfoDao infoDao) {
        this.entityManagerProvider = entityManagerProvider;
        this.infoDao = infoDao;
    }

    /**
     * {@inheritDoc}
     *
     * @see Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        isInterrupted = false;
        if (!entityManagerProvider.isInitialized()) {
            return;
        }
        long start = System.currentTimeMillis();
        MDC.put(Utils.MDC_JOB_METHOD, getName());
        MDC.put(Utils.MDC_REQUEST_ID, String.valueOf(UUID.randomUUID()));

        entityManagerProvider.create();
        final JobStatusInfo statusInfo;
        try {
            final JobStatusInfo info = infoDao.getByName(getName());
            if (info != null && (isRunning(info) || !isEnabled(info))) {
                return;
            }
            logger.info(getName() + " is started");
            statusInfo = initJobStatusInfo(info);
        } finally {
            entityManagerProvider.close();
        }
        try {
            executeLogic();
        } catch (Throwable e) {
            logger.error("Problem executing scheduled job: " + getName(), e);
        } finally {
            if (statusInfo != null) {
                try {
                    entityManagerProvider.create();
                    statusInfo.setStopDate(new Date());
                    statusInfo.setRunning(false);
                    infoDao.update(statusInfo);
                } catch (Throwable e) {
                    logger.error("Cannot update job info. job stopped incorrectly", e);
                } finally {
                    entityManagerProvider.close();
                }
            }
            MDC.put(Utils.MDC_DURATION, String.valueOf(System.currentTimeMillis() - start));
            logger.info(getName() + " is stopped");
            MDC.remove(Utils.MDC_DURATION);
            MDC.remove(Utils.MDC_REQUEST_ID);
            MDC.remove(Utils.MDC_JOB_METHOD);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see org.quartz.InterruptableJob#interrupt()
     */
    @Override
    public void interrupt() throws UnableToInterruptJobException {
        logger.warn(getName() + " is interrupted!");
        this.isInterrupted = true;
    }

    /**
     * Retrieves the {@link JobInfo}
     *
     * @return the job info
     */
    public JobInfo getInfo() {
        entityManagerProvider.create();
        try {
            JobStatusInfo statusInfo = infoDao.getByName(getName());
            if (statusInfo == null)
                return new JobInfo(getName(), getDescription(), null, null, false, true, null);
            else
                return new JobInfo(statusInfo.getName(),
                        getDescription(),
                        statusInfo.getStartDate(),
                        statusInfo.getStopDate(),
                        statusInfo.isRunning(),
                        statusInfo.isEnabled(),
                        statusInfo.getEnabledBy() != null ? statusInfo.getEnabledBy() : null);
        } finally {
            entityManagerProvider.close();
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Retrieves the Job name
     *
     * @return job name
     */
    protected abstract String getName();

    /**
     * Retrieves the Job description
     *
     * @return job description
     */
    protected abstract String getDescription();

    /**
     * Execute job logic
     */
    protected abstract void executeLogic() throws Exception;

    // ------------------------------------------------------------------------
    private boolean isRunning(final JobStatusInfo statusInfo) {
        return statusInfo != null && statusInfo.isRunning();
    }

    private boolean isEnabled(final JobStatusInfo statusInfo) {
        return statusInfo != null && statusInfo.isEnabled();
    }

    private JobStatusInfo initJobStatusInfo(JobStatusInfo statusInfo) {
        if (statusInfo == null) {
            statusInfo = new JobStatusInfo();
        }
        statusInfo.setName(getName());
        statusInfo.setStartDate(new Date());
        statusInfo.setStopDate(null);
        statusInfo.setRunning(true);
        statusInfo.setEnabled(true);
        if (statusInfo.getId() == null)
            return infoDao.create(statusInfo);
        else
            return infoDao.update(statusInfo);
    }


}
