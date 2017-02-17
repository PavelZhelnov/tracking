package com.tmw.tracking.web.hibernate;

import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.utils.Utils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class EntityManagerProvider {

    private final static Logger logger = LoggerFactory.getLogger(EntityManagerProvider.class);


    private final String unitName;
    private EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager;

    public EntityManagerProvider(String unitName) {
        this.unitName = unitName;
        threadLocalEntityManager = new ThreadLocal<EntityManager>();
        entityManagerFactory = Persistence.createEntityManagerFactory(unitName, Utils.getProperties());
    }

    public EntityManager getEntityManager() {
        EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager == null) {
            create();
            entityManager = threadLocalEntityManager.get();
            if (entityManager == null) {
                throw new IllegalStateException("Thread local entityManager is empty, creating new");
            }
        }
        return entityManager;
    }

    public boolean isInitialized() {
        return entityManagerFactory != null;
    }

    public void destroy() {
        entityManagerFactory.close();
    }

    public void create() {
        EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        entityManager = entityManagerFactory.createEntityManager();
        threadLocalEntityManager.set(entityManager);
    }

    public void close() {
        final EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager != null) {
            if (entityManager.isOpen())
                entityManager.close();
            threadLocalEntityManager.remove();
        }
    }

    public void applyGlobalFilters(final User user) {
        if (user == null || getEntityManager() == null) return;
        final Session session = getEntityManager().unwrap(Session.class);
        //TODO
    }

    public void applyGlobalFilters() {
        if (SecurityUtils.getSubject().isAuthenticated())
            applyGlobalFilters(AuthenticationServiceImpl.getAuthenticatedUser());
    }


}
