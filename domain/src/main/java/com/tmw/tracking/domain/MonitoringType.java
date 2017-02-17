package com.tmw.tracking.domain;

public enum MonitoringType {
    TRACKING_DATABASE("Database", false),
    LOGIN("Login", false),
    SEND_EMAIL("Send email", false);

    private String desc;
    private Boolean separate;

    private MonitoringType(String desc, Boolean separate) {
        this.desc = desc;
        this.separate = separate;
    }

    public String getDesc() {
        return desc;
    }

    public Boolean isSeparate() {
        return separate;
    }

    public static boolean isExist(String val) {
        for(MonitoringType me : MonitoringType.values()) {
            if(me.name().equalsIgnoreCase(val))
                return true;
        }
        return false;
    }
}
