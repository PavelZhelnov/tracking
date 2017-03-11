package com.tmw.tracking.guice;

import com.google.inject.AbstractModule;
import com.tmw.tracking.web.controller.AnonController;
import com.tmw.tracking.web.controller.DictController;
import com.tmw.tracking.web.controller.MainController;
import com.tmw.tracking.web.controller.MonitoringController;
import com.tmw.tracking.web.controller.TrackingController;
import com.tmw.tracking.web.controller.UserController;

public class ControllerModule extends AbstractModule {

    /**
     * {@inheritDoc}
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        bind(MainController.class);
        bind(MonitoringController.class);
        bind(UserController.class);
        bind(TrackingController.class);
        bind(AnonController.class);
        bind(DictController.class);
    }
}
