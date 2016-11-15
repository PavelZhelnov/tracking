package com.tmw.tracking.guice;

import com.google.inject.AbstractModule;
import com.tmw.tracking.web.controller.MainController;
import com.tmw.tracking.web.controller.MonitoringController;

/**
 * Freemarker Controller Module
 * @author dmikhalishin@provectus-it.com
 * @see AbstractModule
 */
public class ControllerModule extends AbstractModule {

    /**
     * {@inheritDoc}
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        bind(MainController.class);
        bind(MonitoringController.class);
    }
}
