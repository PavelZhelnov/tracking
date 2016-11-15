package com.tmw.tracking.web.support.log;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.StringMatchFilter;

/**
 * User: enedzvetsky@provectus-it.com
 */
public class ContainsFilter extends StringMatchFilter {
    @Override
    public int decide(LoggingEvent event) {
        String msg = event.getRenderedMessage();
        String stringToMatch = getStringToMatch();
        if(msg == null || stringToMatch == null)
            return Filter.NEUTRAL;
        if(!msg.contains(stringToMatch)) {
            return Filter.NEUTRAL;
        } else { // we've got a match
            if(getAcceptOnMatch()) {
                return Filter.ACCEPT;
            } else {
                return Filter.DENY;
            }
        }

    }
}
