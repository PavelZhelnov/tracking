package com.tmw.tracking.dao;

import com.tmw.tracking.entity.JobStatusInfo;

import java.util.Date;
import java.util.List;

public interface JobStatusInfoDao {

    JobStatusInfo getByName(String name);

    /**
     * Method returns information about all jobs.
     *
     * @return list of JobStatusInfo
     */
    List<JobStatusInfo> getAllJobs();

    JobStatusInfo create(JobStatusInfo statusInfo);

    JobStatusInfo update(JobStatusInfo statusInfo);

    Date getNextRun(String name);
}
