package com.tmw.tracking.job.domain;

import com.tmw.tracking.entity.User;

import java.util.Date;

/**
 * @author dmikhalishin@provectus-it.com
 */

public class JobInfo {

    private final String name;
    private final String description;
    private Date startDate;
    private Date endDate;
    private final boolean running;
    private final boolean enabled;
    private Date nextFireTime;
    private User enabledBy;

    public JobInfo(final String name,
                   final String description,
                   final Date startDate,
                   final Date endDate,
                   final boolean running,
                   final boolean enabled,
                   final User enabledBy){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.running = running;
        this.enabled = enabled;
        this.enabledBy = enabledBy;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isRunning() {
        return running;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User getEnabledBy() {
        return enabledBy;
    }

    public void setEnabledBy(User enabledBy) {
        this.enabledBy = enabledBy;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
