package com.tmw.tracking.web.support.log;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import java.util.Date;

/**
 * We send email with logs one time per day or if buffer has been filled.
 * <p/>
 * Created by ankultepin on 19.03.2015.
 */
public class DigestBasedTriggeringEventEvaluator implements TriggeringEventEvaluator {

    private static final int AMOUNT_HOURS = 24;
    private Date triggerDate = new Date();
    private Date currentDate = new Date();
    private int counter = 0;

    public boolean isTriggeringEvent(LoggingEvent event) {
        counter++;
        currentDate = new Date();
        if (event.getLevel().isGreaterOrEqual(Level.ERROR) &&
                (counter == getBufferSize() || currentDate.after(DateUtils.addHours(triggerDate, AMOUNT_HOURS)))) {
            counter = 0;
            triggerDate = new Date();
            return true;
        } else {
            return false;
        }
    }

    private int getBufferSize() {
        Logger rootLogger = Logger.getRootLogger();
        SMTPAppender smtpAppender = (SMTPAppender) rootLogger.getAppender("mail");
        return smtpAppender.getBufferSize();
    }

}