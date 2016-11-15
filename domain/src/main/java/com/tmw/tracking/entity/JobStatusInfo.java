package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author dmikhalishin@provectus-it.com
 */
@Entity
@Table(schema = "tracking", name="job_status_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStatusInfo implements Serializable {
    private static final long serialVersionUID = -6886848877574564547L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Name cannot be null")
    @Column(nullable = false, name = "name")
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "stop_date")
    private Date stopDate;
    @Column(name = "running")
    @Type(type="yes_no")
    private boolean running;
    @Column(name = "enabled")
    @Type(type="yes_no")
    private boolean enabled;
    @OneToOne
    @JoinColumn(name="enabled_by", nullable = true)
    private User enabledBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User getEnabledBy() {
        return enabledBy;
    }

    public void setEnabledBy(User enabledBy) {
        this.enabledBy = enabledBy;
    }
}
