package com.tmw.tracking.web;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tmw.tracking.utils.GuiceInstanceHolder;
import com.tmw.tracking.utils.Utils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class TrackingContextListener extends GuiceServletContextListener {

    private final static Logger logger = LoggerFactory.getLogger(TrackingContextListener.class);
    private Scheduler scheduler;
    private ExecutorService executorServiceApp;
    private ScheduledExecutorService scheduledExecutorService;
    /**
     * {@inheritDoc}
     * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
     */
    @Override
    protected Injector getInjector() {
        scheduler = GuiceInstanceHolder.getInjector().getInstance(Scheduler.class);
        executorServiceApp = GuiceInstanceHolder.getInjector().getInstance(ExecutorService.class);
        scheduledExecutorService = GuiceInstanceHolder.getInjector().getInstance(ScheduledExecutorService.class);
        return GuiceInstanceHolder.getInjector();
    }

    /**
     * {@inheritDoc}
     * @see com.google.inject.servlet.GuiceServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        logger.info("Context Initializing for tracking.env=" + System.getProperty("tracking.env"));
        TimeZone.setDefault(Utils.DEFAULT_TIMEZONE);
    }

    /**
     * {@inheritDoc}
     * @see com.google.inject.servlet.GuiceServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent){
        super.contextDestroyed(servletContextEvent);
        try {
            scheduler.shutdown(true);
        }catch (SchedulerException e){logger.warn(Utils.errorToString(e));}
        try {
            executorServiceApp.shutdown();
        }catch (Exception e){logger.warn(Utils.errorToString(e));}
        try {
            scheduledExecutorService.shutdown();
        }catch (Exception e){logger.warn(Utils.errorToString(e));}
    }

}
