package com.tmw.tracking.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.tmw.tracking.guice.ControllerModule;
import com.tmw.tracking.guice.DaoModule;
import com.tmw.tracking.guice.ScheduleJobModule;
import com.tmw.tracking.guice.ServiceModule;
import com.tmw.tracking.service.MonitoringService;
import com.tmw.tracking.service.impl.MonitoringServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Guice Instance Holder
 * @author dmikhalishin@provectus-it.com
 */
public class GuiceInstanceHolder {

    private final static Logger logger = LoggerFactory.getLogger(GuiceInstanceHolder.class);

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
            postInit();
        } catch (Exception e) {
            logger.error(Utils.errorToString(e));
            throw  new RuntimeException(e);
        }
    }

    private static void postInit(){
        startTimers();
    }

    private static void startTimers() {
        MonitoringService monitoringLogic = injector.getInstance(MonitoringServiceImpl.class);
        monitoringLogic.startMonitoring();
    }

    private static List<Module> getModules(){
        final List<Module> moduleInstances = new ArrayList<Module>();
        moduleInstances.add(new ServiceModule());
        moduleInstances.add(new DaoModule());
        moduleInstances.add(new ControllerModule());
        moduleInstances.add(new ScheduleJobModule());
        return moduleInstances;
    }
}
