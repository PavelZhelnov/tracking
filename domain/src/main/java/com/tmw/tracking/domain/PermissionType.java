package com.tmw.tracking.domain;

public enum PermissionType {
    SHOW_TRANSACTIONS("Show all transactions"),
    SHOW_DETAILS("Show details on transaction"),
    SHOW_WORKFLOW("Show the workflow"),
    SHOW_DICTIONARIES("Show dictionaries"),
    SHOW_ROLES("Role managements"),
    SHOW_USERS("User management"),
    SHOW_STATUS("Status page"),
    SHOW_LOGS("Logs page"),
    LOGIN_APP("Login availability to site"),
    JOB_STATUS("Access to jobs page"),
    SHOW_SYSTEM_CONFIG_MANAGEMENT("Configuration"),
    SHOW_SYSTEM_MONITORING("System monitoring and tests");

    private String description;

    PermissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
