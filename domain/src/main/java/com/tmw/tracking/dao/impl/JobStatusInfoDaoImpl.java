package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.JobStatusInfoDao;
import com.tmw.tracking.entity.JobStatusInfo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Implementation {@link JobStatusInfoDao}
 * @author dmikhalishin@provectus-it.com
 * @see JobStatusInfoDao
 */
@Singleton
public class JobStatusInfoDaoImpl implements JobStatusInfoDao {

    private EntityManager entityManager;
    @Inject
    public JobStatusInfoDaoImpl(final EntityManager entityManager){
        this.entityManager = entityManager;
    }
    /**
     * {@inheritDoc}
     * @see JobStatusInfoDao#create(JobStatusInfo)
     */
    @Override
    public JobStatusInfo getByName(final String name) {
        final Query query = entityManager.createQuery("from JobStatusInfo where name = :name");
        query.setParameter("name", name);
        try {
            return (JobStatusInfo)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see JobStatusInfoDao#getAllJobs()
     */
    @Override
    public List<JobStatusInfo> getAllJobs() {
        final Query query = entityManager.createQuery("from JobStatusInfo");
        return query.getResultList();
    }


    /**
     * {@inheritDoc}
     * @see JobStatusInfoDao#create(JobStatusInfo)
     */
    @Transaction
    @Override
    public JobStatusInfo create(final JobStatusInfo statusInfo) {
        entityManager.persist(statusInfo);
        return statusInfo;
    }
    /**
     * {@inheritDoc}
     * @see JobStatusInfoDao#update(JobStatusInfo)
     */
    @Transaction
    @Override
    public JobStatusInfo update(final JobStatusInfo statusInfo) {
        return entityManager.merge(statusInfo);
    }

    @Override
    public Date getNextRun(String jobName) {
        Query query = entityManager.createNativeQuery("Select NEXT_FIRE_TIME from tracking.Qrtz_Triggers where JOB_NAME = '"+jobName+"' AND Trigger_Name = '"+jobName+"'");
        BigDecimal date = ((BigDecimal)query.getSingleResult());
        return new Date(date.longValue());
    }
}
