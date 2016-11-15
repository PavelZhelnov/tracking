package com.tmw.tracking.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * User: enedzvetsky@provectus-it.com
 */
public class TimeZoneUtils {
    private final static Logger logger = LoggerFactory.getLogger(TimeZoneUtils.class);
    private final static Set<String> availableTimeZoneCodes = new HashSet<String>();
    static {
        String[] availableIDs = TimeZone.getAvailableIDs();
        availableTimeZoneCodes.addAll(Arrays.asList(availableIDs));
    }

    public static boolean isAvailableTimeZone(String timeZoneId){
        return availableTimeZoneCodes.contains(timeZoneId);
    }

}
