package com.tmw.tracking.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.tmw.tracking.guice.DaoModule;
import com.tmw.tracking.guice.ServiceModule;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.service.impl.PermissionServiceImpl;
import com.tmw.tracking.service.impl.TrackingServiceImpl;
import com.tmw.tracking.service.impl.UserServiceImpl;
import com.tmw.tracking.utils.DynamicConfig;
import com.tmw.tracking.utils.GuiceInstanceHolder;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.aop.ServiceMethodInterceptor;
import com.tmw.tracking.web.hibernate.EntityManagerFlowFilter;
import com.tmw.tracking.web.service.auth.AuthenticationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiceInstanceHolderTest {

    private final static Logger logger = LoggerFactory.getLogger(GuiceInstanceHolder.class);
    private static final int N_THREADS = 1;

    private static Injector injector;

    public synchronized static Injector getInjector(){
        if(injector == null) {
            initInjector();
        }
        return injector;
    }

    private static void initInjector(){
        try {
            logger.info("*******Start initialize the Guice*******************");
            final List<Module> moduleInstances = getModules();
            injector = Guice.createInjector(moduleInstances);
            logger.info("*******Guice successfully initialized*******");
        } catch (Exception e) {
            logger.error(Utils.errorToString(e));
            throw  new RuntimeException(e);
        }
    }

    private static List<Module> getModules(){
        final List<Module> moduleInstances = new ArrayList<Module>();
        moduleInstances.add(new JerseyServletModule() {
            /**
             * {@inheritDoc}
             * @see com.sun.jersey.guice.JerseyServletModule#configureServlets()
             */
            @Override
            protected void configureServlets() {
                // Must configure at least one JAX-RS resource or the
                // server will fail to start.
                bind(AuthenticationResource.class);

                final Properties properties = Utils.getProperties();
                Names.bindProperties(binder(), properties);

                final Object threadPoolSize = properties.getProperty(ServiceModule.THREAD_SIZE_KEY);
                bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(threadPoolSize != null && Utils.isLong(threadPoolSize.toString())
                        ? Integer.valueOf(threadPoolSize.toString()) : 2));

                bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
                bind(PermissionService.class).to(PermissionServiceImpl.class);
                bind(UserService.class).to(UserServiceImpl.class);
                bind(TrackingService.class).to(TrackingServiceImpl.class);

                final ServiceMethodInterceptor serviceMethodInterceptor = new ServiceMethodInterceptor();
                bindInterceptor(Matchers.any(), Matchers.annotatedWith(GET.class), serviceMethodInterceptor);
                bindInterceptor(Matchers.any(), Matchers.annotatedWith(POST.class), serviceMethodInterceptor);
                bindInterceptor(Matchers.any(), Matchers.annotatedWith(DELETE.class), serviceMethodInterceptor);
                bindInterceptor(Matchers.any(), Matchers.annotatedWith(PUT.class), serviceMethodInterceptor);

                // Route all requests through GuiceContainer
                final Map<String, String> params = new HashMap<String, String>();
                params.put("com.sun.jersey.spi.container.ContainerResponseFilters", "com.tmw.tracking.web.service.util.response.ResponseFilter");
                filter("/*").through(EntityManagerFlowFilter.class);
                serve("/webresources/*").with(GuiceContainer.class, params);
            }

            @Singleton
            @Provides
            @Named("default")
            public ExecutorService executorServiceDefault() {
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

        });

        moduleInstances.add(new DaoModule());
//        moduleInstances.add(new ValidationModule());

        return moduleInstances;
    }



}
