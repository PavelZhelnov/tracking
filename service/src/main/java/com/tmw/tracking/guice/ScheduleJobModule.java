package com.tmw.tracking.guice;

import org.nnsoft.guice.guartz.QuartzModule;

/**
 * Schedule Job Module
 * @author dmikhalishin@provectus-it.com
 * @see QuartzModule
 */
public class ScheduleJobModule extends QuartzModule {

    private static final String DEFAULT_SCHEDULE = "0 0 0 1 1 ?";

    /**
     * {@inheritDoc}
     * @see org.nnsoft.guice.guartz.QuartzModule#schedule()
     */
    @Override
    protected void schedule() {
        /*Properties properties = Utils.getProperties();
        configureScheduler().withProperties(properties);
        String schedule = properties.getProperty(Utils.ORDER_GARBAGE_CRON);
        scheduleJob(GarbageCollectorJob.class).withCronExpression(StringUtils.isBlank(schedule) ? DEFAULT_SCHEDULE : schedule).updateExistingTrigger();


        final Multibinder<TrackingJob> jobBinder = Multibinder.newSetBinder(binder(), TrackingJob.class);
        jobBinder.addBinding().to(GarbageCollectorJob.class);*/

    }




}
