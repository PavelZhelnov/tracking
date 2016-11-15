package com.tmw.tracking.web.support.log;

import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * User: enedzvetsky@provectus-it.com
 */
public class ServiceExcLogFilter extends Filter {

    @Override
    public int decide(LoggingEvent event) {
        ThrowableInformation throwableInformation = event.getThrowableInformation();
        if(throwableInformation==null){
            return Filter.NEUTRAL;
        }
        Throwable throwable = throwableInformation.getThrowable();
        if(throwable instanceof ServiceException){
            if(!((ServiceException)throwable).isImportant()){
                return Filter.DENY;
            }
        }
        return Filter.NEUTRAL;

    }
}
