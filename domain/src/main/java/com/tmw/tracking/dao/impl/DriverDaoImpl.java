package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.entity.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by pzhelnov on 2/8/2017.
 */
@Singleton
public class DriverDaoImpl implements DriverDao {


    private static final Logger logger = LoggerFactory.getLogger(DriverDaoImpl.class);

    private EntityManager entityManager;

    @Inject
    public DriverDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Driver getById(Long id) {

        TypedQuery<Driver> query = entityManager.createQuery("from Driver where id = :id ", Driver.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Driver getByMobile(String mobile) {

        TypedQuery<Driver> query = entityManager.createQuery("from Driver where mobile= :mobile", Driver.class);
        query.setParameter("mobile", mobile);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Driver> getDriversByName(String name) {
        TypedQuery<Driver> query = entityManager.createQuery("from Driver where lastName = :name or firstName = :name", Driver.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public Driver create(Driver driver) {
        Driver existing = getByMobile(driver.getMobile());
        if (existing != null) {
            throw new RuntimeException("Driver "+ driver.getMobile() + " already exists!");
        }
        entityManager.persist(driver);
        return driver;
    }

    @Override
    public Driver update(Driver driver) {
        return entityManager.merge(driver);
    }

    @Override
    public void delete(Driver driver) {
        entityManager.remove(driver);

    }

    @Override
    public List<Driver> getAll() {
        return null;
    }
}
