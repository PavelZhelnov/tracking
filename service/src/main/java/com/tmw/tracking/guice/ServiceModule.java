package com.tmw.tracking.guice;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.freemarker.FreemarkerViewProcessor;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.MonitoringService;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.service.impl.MonitoringServiceImpl;
import com.tmw.tracking.service.impl.PermissionServiceImpl;
import com.tmw.tracking.service.impl.TrackingServiceImpl;
import com.tmw.tracking.service.impl.UserServiceImpl;
import com.tmw.tracking.utils.DynamicConfig;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.TrackingGuiceContainer;
import com.tmw.tracking.web.aop.LogDuration;
import com.tmw.tracking.web.aop.LogDurationInterceptor;
import com.tmw.tracking.web.aop.MethodCall;
import com.tmw.tracking.web.aop.MethodCallInterceptor;
import com.tmw.tracking.web.aop.ServiceMethodInterceptor;
import com.tmw.tracking.web.hibernate.EntityManagerFlowFilter;
import com.tmw.tracking.web.service.auth.AuthenticationResource;
import com.tmw.tracking.web.service.user.UserResource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Service module
 *
 * @author dmikhalishin@provectus-it.com
 * @see JerseyServletModule
 */
public class ServiceModule extends JerseyServletModule {

    public static final String THREAD_SIZE_KEY = "thread.size";
    public static final String TEST_MODE = "tracking.test.mode";
    public static final String DEBUG_MODE = "tracking.debug.mode";
    public static final int N_THREADS = 10;
    public static final String TRACKING_ENV = "tracking.env";

    /**
     * {@inheritDoc}
     *
     * @see com.sun.jersey.guice.JerseyServletModule#configureServlets()
     */
    @Override
    protected void configureServlets() {
        // Must configure at least one JAX-RS resource or the
        // server will fail to start.
        bind(AuthenticationResource.class);
        bind(UserResource.class);

        // test mode
        final Object testMode = System.getProperty(TEST_MODE);
        final Object debugeMode = System.getProperty(DEBUG_MODE);
        final Properties properties = Utils.getProperties();

        Names.bindProperties(binder(), properties);
        final Properties propertiesServerVersion = Utils.getServerVersionProperties();
        Names.bindProperties(binder(), propertiesServerVersion);


        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
        bind(PermissionService.class).to(PermissionServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(MonitoringService.class).to(MonitoringServiceImpl.class);
        bind(TrackingService.class).to(TrackingServiceImpl.class);
        /* use real services */
        //bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
        //bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderProvider.class).in(Singleton.class);

        final Object threadPoolSize = properties.getProperty(THREAD_SIZE_KEY);
        bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(threadPoolSize != null && Utils.isLong(threadPoolSize.toString())
                ? Integer.valueOf(threadPoolSize.toString()) : N_THREADS));
        bind(ScheduledExecutorService.class).toInstance(Executors.newSingleThreadScheduledExecutor());
        final ServiceMethodInterceptor serviceMethodInterceptor = new ServiceMethodInterceptor();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(GET.class), serviceMethodInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(POST.class), serviceMethodInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(DELETE.class), serviceMethodInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(PUT.class), serviceMethodInterceptor);

        final LogDurationInterceptor logDurationInterceptor = new LogDurationInterceptor();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(LogDuration.class), logDurationInterceptor);

        final MethodCallInterceptor methodCallInterceptor = new MethodCallInterceptor();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(MethodCall.class), methodCallInterceptor);

        // Route all requests through GuiceContainer
        final Map<String, String> params = new HashMap<String, String>();
        if (debugeMode != null && Boolean.valueOf(debugeMode.toString())) {
            params.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, "com.sun.jersey.api.container.filter.LoggingFilter");
            params.put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, "com.tmw.tracking.web.service.utils.response.ResponseFilter,com.sun.jersey.api.container.filter.LoggingFilter");
        } else {
            params.put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, "com.tmw.tracking.web.service.utils.response.ResponseFilter");
        }
//        params.put("com.sun.jersey.config.feature.Trace", "true");               // uncomment if need trace rest calls
        params.put(LoggingFilter.FEATURE_LOGGING_DISABLE_ENTITY, "false"); //no, uncomment this, as we need rest calls only, without any other garbage

        filter("/*").through(EntityManagerFlowFilter.class);
        serve("/webresources/*").with(GuiceContainer.class, params);
        final Map<String, String> pages = new HashMap<String, String>();
        pages.put(FreemarkerViewProcessor.FREEMARKER_TEMPLATES_BASE_PATH, "templates");
        serve("/tmw/*").with(TrackingGuiceContainer.class, pages);
    }

    @Singleton
    @Provides
    @Named("default")
    public ExecutorService getDefaultExecutorServiceForPOS() {
        return Executors.newFixedThreadPool(N_THREADS);
    }


    @Singleton
    @Provides
    public DynamicConfig dynamicConfig() {
        final Properties properties = Utils.getProperties();
        final DynamicConfig dynamicConfig = new DynamicConfig();
        dynamicConfig.setAllowSendMail(Boolean.valueOf(properties.getProperty("mail.allow.send")));
        dynamicConfig.setAllowPrint(Boolean.valueOf(properties.getProperty("printer.allow.print")));
        return dynamicConfig;
    }


}
