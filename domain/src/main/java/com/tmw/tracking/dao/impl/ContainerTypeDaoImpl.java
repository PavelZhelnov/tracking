package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.ContainerTypeDao;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.entity.ContainerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class ContainerTypeDaoImpl implements ContainerTypeDao {

    private final static Logger logger = LoggerFactory.getLogger(ContainerTypeDao.class);

    private EntityManager entityManager;

    @Inject
    public ContainerTypeDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @see RoleDao#getById(Long)
     */
    @Override
    public ContainerType getById(final Long id) {
        final TypedQuery<ContainerType> query = entityManager.createQuery("from ContainerType where id = :id", ContainerType.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transaction
    @Override
    public ContainerType update(ContainerType containerType) {
        if (containerType.getId() == null) {
            entityManager.persist(containerType);
        } else {
            entityManager.merge(containerType);
        }
        return containerType;
    }

    @Transaction
    @Override
    public void delete(ContainerType containerType) {
        entityManager.remove(containerType);
    }

    @Override
    public List<ContainerType> getAll() {
        final TypedQuery<ContainerType> query = entityManager.createQuery("from ContainerType order by type", ContainerType.class);
        return query.getResultList();
    }

}
