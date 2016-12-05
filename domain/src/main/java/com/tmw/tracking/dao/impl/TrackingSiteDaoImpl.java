package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.dao.TrackingSiteDao;
import com.tmw.tracking.entity.TrackingSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by pzhelnov on 11/19/2016.
 */
@Singleton
public class TrackingSiteDaoImpl implements TrackingSiteDao {
    private final static Logger logger = LoggerFactory.getLogger(RoleDao.class);

    private EntityManager entityManager;

    @Inject
    public TrackingSiteDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<TrackingSite> getTrackingSites() {
        final TypedQuery<TrackingSite> query = entityManager.createQuery("from TrackingSite", TrackingSite.class);
        return query.getResultList();
    }
}
