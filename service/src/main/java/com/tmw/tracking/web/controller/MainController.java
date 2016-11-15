package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.dao.JobStatusInfoDao;
import com.tmw.tracking.entity.JobStatusInfo;
import com.tmw.tracking.job.TrackingJob;
import com.tmw.tracking.job.domain.JobInfo;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author dmikhalishin@provectus-it.com
 */
@Path("/")
@Singleton
public class MainController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);
    public static final String TIME_ZONE_ID = "UTC";

    private final String serverVersion;
    private final JobStatusInfoDao jobStatusInfoDao;
    private final Set<TrackingJob> trackingJobs;
    private final Scheduler scheduler;

    @Inject
    public MainController(@Named("server.version") final String serverVersion,
                          @Named("major.version") final String majorVersion,
                          final JobStatusInfoDao jobStatusInfoDao,
                          final Set<TrackingJob> trackingJobs,
                          final Scheduler scheduler) {
        this.serverVersion = majorVersion+"_"+serverVersion;
        this.jobStatusInfoDao = jobStatusInfoDao;
        this.trackingJobs = trackingJobs;
        this.scheduler = scheduler;
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    public Viewable loginGet() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        return new Viewable("/login", vars);
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    public Viewable loginPost(@Context final javax.servlet.http.HttpServletRequest req) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        vars.put("error", req.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME) != null);
        vars.put("username", req.getParameter("username"));
        return new Viewable("/login", vars);
    }

    // ------------------------------------------------------------------------

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/status")
    public Viewable getStatusInfo() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("version", serverVersion);
        vars.put("environment", environment);
        final List<JobInfo> jobInfo = new ArrayList<JobInfo>();
        for (final TrackingJob job : trackingJobs) {
            final JobInfo info = job.getInfo();
            info.setStartDate(Utils.getDateInTimeZone(info.getStartDate(), TIME_ZONE_ID));
            info.setEndDate(Utils.getDateInTimeZone(info.getEndDate(), TIME_ZONE_ID));
            try {
                final Trigger trigger = scheduler.getTrigger(new TriggerKey(info.getName()));
                if (trigger != null)
                    info.setNextFireTime(trigger.getNextFireTime());
            } catch (SchedulerException e) {/*do nothing*/}
            jobInfo.add(info);
        }
        vars.put("jobInfo", jobInfo);
        vars.put("angular", true);
        return new Viewable("/status", vars);
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/startJob")
    public String startJob(@QueryParam("key") final String jobName) {
        if (StringUtils.isNotBlank(jobName)) {
            for (final TrackingJob job : trackingJobs) {
                if (job.getInfo().getName().equals(jobName)) {
                    try {
                        final JobKey key = new JobKey(jobName);
                        scheduler.triggerJob(key);
                        return Boolean.TRUE.toString();
                    } catch (SchedulerException e) {
                        logger.error(Utils.errorToString(e));
                    }
                }
            }
        }
        return Boolean.FALSE.toString();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/enableJob")
    public String enableJob(@QueryParam("key") final String jobName, @QueryParam("enabled") final Boolean enabled) {
        if (StringUtils.isNotBlank(jobName)) {
            final JobStatusInfo statusInfo = jobStatusInfoDao.getByName(jobName);
            if (statusInfo != null) {
                if (enabled)
                    statusInfo.setRunning(false);
                statusInfo.setEnabled(enabled);
                statusInfo.setEnabledBy(AuthenticationServiceImpl.getAuthenticatedUser());
                jobStatusInfoDao.update(statusInfo);
            } else {
                logger.error("Job with name " + jobName + " was not found.");
            }
            return Boolean.TRUE.toString();
        }
        return Boolean.FALSE.toString();
    }


}