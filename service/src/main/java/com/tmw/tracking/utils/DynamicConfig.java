package com.tmw.tracking.utils;

/**
 * Created by vandreev on 7/28/2014.
 */
public class DynamicConfig {

    private boolean isAllowPrint;
    private boolean isAllowSendMail;

    public boolean isAllowPrint() {
        return isAllowPrint;
    }

    public void setAllowPrint(boolean isAllowPrint) {
        this.isAllowPrint = isAllowPrint;
    }

    public boolean isAllowSendMail() {
        return isAllowSendMail;
    }

    public void setAllowSendMail(boolean isAllowSendMail) {
        this.isAllowSendMail = isAllowSendMail;
    }

}
