package com.tmw.tracking.web;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.WebConfig;
import com.tmw.tracking.web.support.CustomFreemarkerViewProcessor;

import javax.inject.Inject;
import javax.servlet.ServletException;
import java.util.Map;

@Singleton
public class TrackingGuiceContainer extends GuiceContainer {

    @Inject
    public TrackingGuiceContainer(final Injector injector) {
        super(injector);
    }

    /**
     * {@inheritDoc}
     * @see TrackingGuiceContainer#getDefaultResourceConfig(java.util.Map, com.sun.jersey.spi.container.servlet.WebConfig)
     */
    @Override
    protected ResourceConfig getDefaultResourceConfig(final Map<String, Object> props,
                                                      final WebConfig webConfig) throws ServletException {
        return new DefaultResourceConfig(CustomFreemarkerViewProcessor.class);
    }
}
