package com.tmw.tracking.web.support.logstash;

import org.apache.log4j.net.SMTPAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pzhelnov on 5/18/2016.
 */
public class MWHSMTPAppender extends SMTPAppender {

    private final static Logger logger = LoggerFactory.getLogger(MWHSMTPAppender.class);
    @Override
    public void activateOptions() {
        super.activateOptions();

        String to = getTo();
        //MDC.put(Utils.MDC_SMTP_APPENDER_EMAIL_TO, to);
        logger.info("MWHSMTPAppender executed. Appender mailTo: " + to);

    }
}
