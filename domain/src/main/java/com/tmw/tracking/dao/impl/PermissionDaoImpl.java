package com.tmw.tracking.dao.impl;

import com.tmw.tracking.dao.PermissionDao;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.entity.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by pzhelnov on 2/21/2017.
 */
public class PermissionDaoImpl implements PermissionDao {

    private static final Logger logger = LoggerFactory.getLogger(PermissionDaoImpl.class);

    private EntityManager entityManager;

    @Inject
    public PermissionDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Permission getByPermissionType(PermissionType permissionType) {
        TypedQuery<Permission> query = entityManager.createQuery("from Permission where name = :name ", Permission.class);
        query.setParameter("name", permissionType);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public List<Permission> getAllPermissions() {
        TypedQuery<Permission> query = entityManager.createQuery("from Permission", Permission.class);
        return query.getResultList();
    }
}
