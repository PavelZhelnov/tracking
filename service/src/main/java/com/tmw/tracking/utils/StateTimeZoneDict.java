package com.tmw.tracking.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * User: enedzvetsky@provectus-it.com
 */
public class StateTimeZoneDict {
    public static final Map<String,TimeZone> TIME_ZONE_MAP = new LinkedHashMap<String, TimeZone>();
    static{
        TIME_ZONE_MAP.put("AL", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("AK", TimeZone.getTimeZone("US/Alaska"));
        TIME_ZONE_MAP.put("AZ", TimeZone.getTimeZone("US/Arizona"));
        TIME_ZONE_MAP.put("AR", TimeZone.getTimeZone("US/Mountain"));
        TIME_ZONE_MAP.put("CA", TimeZone.getTimeZone("PST"));
        TIME_ZONE_MAP.put("CO", TimeZone.getTimeZone("US/Mountain"));
        TIME_ZONE_MAP.put("CT", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("DE", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("DC", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("FL", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("GA", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("HI", TimeZone.getTimeZone("US/Hawaii"));
        TIME_ZONE_MAP.put("ID", TimeZone.getTimeZone("US/Mountain"));
        TIME_ZONE_MAP.put("IL", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("IN", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("IA", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("KS", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("KY", TimeZone.getTimeZone("US/Central"));//???
        TIME_ZONE_MAP.put("LA", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("ME", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("MD", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("MA", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("MI", TimeZone.getTimeZone("US/Michigan"));
        TIME_ZONE_MAP.put("MN", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("MS", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("MO", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("MT", TimeZone.getTimeZone("US/Mountain"));
        TIME_ZONE_MAP.put("NE", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("NV", TimeZone.getTimeZone("PST"));
        TIME_ZONE_MAP.put("NH", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("NJ", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("NM", TimeZone.getTimeZone("US/Mountain"));//??
        TIME_ZONE_MAP.put("NY", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("NC", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("ND", TimeZone.getTimeZone("US/Central"));//??
        TIME_ZONE_MAP.put("OH", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("OK", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("OR", TimeZone.getTimeZone("PST"));
        TIME_ZONE_MAP.put("PA", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("RI", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("SC", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("SD", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("TN", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("TX", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("UT", TimeZone.getTimeZone("US/Mountain"));
        TIME_ZONE_MAP.put("VT", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("VA", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("WA", TimeZone.getTimeZone("PST"));
        TIME_ZONE_MAP.put("WV", TimeZone.getTimeZone("US/Eastern"));
        TIME_ZONE_MAP.put("WI", TimeZone.getTimeZone("US/Central"));
        TIME_ZONE_MAP.put("WY", TimeZone.getTimeZone("US/Mountain"));
    }

    public static TimeZone getTimeZone(String stateCode){
        return TIME_ZONE_MAP.get(stateCode);
    }

}
