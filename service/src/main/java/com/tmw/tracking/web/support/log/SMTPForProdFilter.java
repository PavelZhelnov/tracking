package com.tmw.tracking.web.support.log;

import org.apache.log4j.MDC;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.StringMatchFilter;

import com.google.inject.Singleton;
import com.tmw.tracking.utils.Utils;

@Singleton
public class SMTPForProdFilter extends StringMatchFilter {

    @Override
    public int decide(LoggingEvent event) {
        if(!"prod".equalsIgnoreCase(System.getProperty("tracking.env"))){
            return Filter.DENY;
        }
        MDC.put(Utils.MDC_IP_ADDRESS, Utils.getIPAddress());
        return Filter.NEUTRAL;
    }
}
