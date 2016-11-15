package com.tmw.tracking.utils;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import javax.annotation.PostConstruct;
import java.net.URL;

/**
 * Created by pzhelnov on 8/11/2016.
 */
public class ApplicationInitialization {

    public static final String DEV = "dev";
    public static final String LOG4J = "log4j";
    public static final String XML = ".xml";

    @PostConstruct
    public void contextInitialized() {
        //logger init
        String env = System.getProperties().getProperty("tracking.env");
        if (env == null) {
            env = DEV;
        }
        String log4JPropertyFile = LOG4J + "-" + env + XML;
        URL resUrl = getClass().getClassLoader().getResource(log4JPropertyFile);
        PropertyConfigurator.configure(resUrl);
        DOMConfigurator.configure(resUrl);
    }

}
